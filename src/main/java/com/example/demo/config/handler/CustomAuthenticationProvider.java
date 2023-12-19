package com.example.demo.config.handler;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.dto.UserDetailsDto;

import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;

    @NonNull
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("2. CustomAuthenticationProvider");
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        log.info("2. token=" + token);
        String userId = token.getName();
        String userPw = (String) token.getCredentials();

        UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(userId);

        if (userDetailsDto == null) {
            // throw new BadCredentialsException(userId + " Not Found");
            throw new BadCredentialsException("????");
        } else {
            if (!(userDetailsDto.getPassword().equalsIgnoreCase(userPw)
                    && userDetailsDto.getPassword().equalsIgnoreCase(userPw))) {
                throw new BadCredentialsException(userDetailsDto.getUsername() + " Invalid password");
            }
        }

        return new UsernamePasswordAuthenticationToken(userDetailsDto, userPw, userDetailsDto.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
