package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {

		http.csrf((csrfConfig) -> csrfConfig.disable())
				.headers((headerConfig) -> headerConfig
						.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
				// .sessionManagement(
				// sessionManagement ->
				// sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(
								PathRequest.toH2Console(),
								new MvcRequestMatcher(introspector, "/login"),
								new MvcRequestMatcher(introspector, "/login-proc"))
						.permitAll()
						.anyRequest().authenticated())
				// .formLogin(withDefaults());
				.formLogin(formLogin -> formLogin.loginPage("/login").permitAll()
						.failureForwardUrl("/login?error"))
				.addFilterBefore(customAuthenticationFilter2(),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(customAuthenticationProvider());
	}

	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider(bCryptPasswordEncoder());
	}

	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomAuthenticationFilter customAuthenticationFilter2() {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
		customAuthenticationFilter.setFilterProcessesUrl("/login-proc");
		customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
		customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());
		customAuthenticationFilter.afterPropertiesSet();
		return customAuthenticationFilter;
	}

	@Bean
	public CustomAuthSuccessHandler customLoginSuccessHandler() {
		return new CustomAuthSuccessHandler();
	}

	@Bean
	public CustomAuthFailureHandler customLoginFailureHandler() {
		return new CustomAuthFailureHandler();
	}

}
