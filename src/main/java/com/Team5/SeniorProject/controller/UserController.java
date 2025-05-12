package com.Team5.SeniorProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
		return userRepository.findByUsername(username)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}
	
}
