package com.example.demo.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDetailsDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) {
        User userDto = User
                .builder()
                .userId(userId)
                .build();

        Optional<User> userInfo = userRepository.findById(userDto.getUserId());
        if (userInfo.isEmpty()) {
            return null;
        }

        // 사용자 정보가 존재하지 않는 경우
        if (userId == null || userId.equals("")) {
            return userInfo
                    .map(u -> new UserDetailsDto(u, Collections.singleton(new SimpleGrantedAuthority(u.getUserId()))))
                    .orElseThrow(() -> new AuthenticationServiceException(userId));
        }
        // 비밀번호가 맞지 않는 경우
        else {
            return userInfo
                    .map(u -> new UserDetailsDto(u, Collections.singleton(new SimpleGrantedAuthority(u.getUserId()))))
                    .orElseThrow(() -> new BadCredentialsException(userId));
        }
    }

}
