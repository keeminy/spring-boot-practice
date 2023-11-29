package com.example.demo.example.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	public Optional<User> login(User user) {
		log.debug("user : " + user.toString());
		Optional<User> userInfo = userRepository.findById(user.getUserId());

		log.debug("userInfo : " + userInfo.toString());
		return userInfo;
	}
}
