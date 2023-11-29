package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.demo.config.filter.CustomAuthenticationFilter;
import com.example.demo.config.handler.CustomAuthFailureHandler;
import com.example.demo.config.handler.CustomAuthSuccessHandler;
import com.example.demo.config.handler.CustomAuthenticationProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**", "/sign-up", "/login"};	// sign-up, sign-in 추가



	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		log.debug("PathRequest.toStaticResources().atCommonLocations() : " + PathRequest.toStaticResources().atCommonLocations());
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {

		http
			.csrf((csrfConfig) ->
				csrfConfig.disable()
			)
			.headers(
				(headerConfig) ->
					headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()
				)
			)
			.authorizeHttpRequests((requests) ->
				requests
					.requestMatchers(PathRequest.toH2Console()).permitAll()
					.requestMatchers(new MvcRequestMatcher(introspector, "/"),
							new MvcRequestMatcher(introspector, "/home")).permitAll()
					.anyRequest().authenticated())
			.formLogin((formLogin) ->
				formLogin
					.loginPage("/login")
					.defaultSuccessUrl("/", true))
			.logout((logoutConfig) ->
				logoutConfig.logoutSuccessUrl("/")
			)
			.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
			;

		return http.build();
	}

	// @Bean
	// public UserDetailsService userDetailsService() {
	// UserDetails user =
	// User.withDefaultPasswordEncoder().username("user").password("password")
	// .roles("USER").build();

	// return new InMemoryUserDetailsManager(user);
	// }

	/**
	 * 3. authenticate 의 인증 메서드를 제공하는 매니져로'Provider'의 인터페이스를 의미합니다.
	 * - 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
	 *
	 * @return AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(customAuthenticationProvider());
	}

	/**
	 * 4. '인증' 제공자로 사용자의 이름과 비밀번호가 요구됩니다.
	 * - 과정: CustomAuthenticationFilter → AuthenticationManager(interface) → CustomAuthenticationProvider(implements)
	 *
	 * @return CustomAuthenticationProvider
	 */
	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider(bCryptPasswordEncoder());
	}

	/**
	 * 5. 비밀번호를 암호화하기 위한 BCrypt 인코딩을 통하여 비밀번호에 대한 암호화를 수행합니다.
	 *
	 * @return BCryptPasswordEncoder
	 */
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 6. 커스텀을 수행한 '인증' 필터로 접근 URL, 데이터 전달방식(form) 등 인증 과정 및 인증 후 처리에 대한 설정을 구성하는
	 * 메서드입니다.
	 *
	 * @return CustomAuthenticationFilter
	 */
	@Bean
	public CustomAuthenticationFilter customAuthenticationFilter() {


		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
		customAuthenticationFilter.setFilterProcessesUrl("/api/v1/user/login"); // 접근 URL 성공 시 해당 핸들러로 처리를 전가한다.
		customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());  // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
		customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler()); // '인증' 실패 시 해당 핸들러로 처리를전가한다.
		customAuthenticationFilter.afterPropertiesSet();
		return customAuthenticationFilter;
	}

	/**
	 * 7. Spring Security 기반의 사용자의 정보가 맞을 경우 수행이 되며 결과값을 리턴해주는 Handler
	 *
	 * @return CustomLoginSuccessHandler
	 */
	@Bean
	public CustomAuthSuccessHandler customLoginSuccessHandler() {
		return new CustomAuthSuccessHandler();
	}

	/**
	 * 8. Spring Security 기반의 사용자의 정보가 맞지 않을 경우 수행이 되며 결과값을 리턴해주는 Handler
	 *
	 * @return CustomAuthFailureHandler
	 */
	@Bean
	public CustomAuthFailureHandler customLoginFailureHandler() {
		return new CustomAuthFailureHandler();
	}

	/**
	 * 9. JWT 토큰을 통하여서 사용자를 인증합니다.
	 *
	 * @return JwtAuthorizationFilter
	 */
	// @Bean
	// public JwtAuthorizationFilter jwtAuthorizationFilter() {
	// return new JwtAuthorizationFilter();
	// }
}
