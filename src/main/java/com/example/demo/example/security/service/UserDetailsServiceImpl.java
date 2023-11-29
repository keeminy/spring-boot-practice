package com.example.demo.example.security.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.example.security.model.UserDetailsDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
    UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) {
    	log.debug("UserDetailsServiceImpl :  " +  userId);
    	User userDto = User.builder().userId(userId).build();
    	log.debug("userDto :  " +  userDto.toString());

         if (userId == null || userId.equals("")) {
            return userService.login(userDto)
                    .map(user -> new UserDetailsDto(user,
                            Collections.singleton(new SimpleGrantedAuthority(user.getUserId()))))
                    .orElseThrow(() -> new AuthenticationServiceException(userId));
        } else {
            return userService.login(userDto)
                    .map(user -> new UserDetailsDto(user,
                            Collections.singleton(new SimpleGrantedAuthority(user.getUserId()))))
                    .orElseThrow(() -> new BadCredentialsException(userId));
        }
    }
}
