package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  // https://spring.io/guides/gs/securing-web/

  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    HandlerMappingIntrospector introspector
  ) throws Exception {
    http
      .authorizeHttpRequests(requests ->
        requests
          .requestMatchers(
            new MvcRequestMatcher(introspector, "/"),
            new MvcRequestMatcher(introspector, "/home")
          )
          .permitAll()
          .anyRequest()
          .authenticated()
      )
      .formLogin(form -> form.loginPage("/login").permitAll())
      .logout(logout -> logout.permitAll());

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user = User
      .withDefaultPasswordEncoder()
      .username("user")
      .password("password")
      .roles("USER")
      .build();

    return new InMemoryUserDetailsManager(user);
  }
}
