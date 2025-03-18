package com.Team5.SeniorProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;

@RestController
@RequestMapping("/api/SignupController")
@CrossOrigin(origins = "http://localhost:5173") 
public class SignupController {

	private final UserRepository userRepository;

	public SignupController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@PostMapping
	public String signup(@RequestBody User user) {
		// Simulate saving user (later replace with a database)
		userRepository.save(user);
		return "User signed up successfully!";
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}

	@PostMapping("/login")
	public String login(@RequestBody User loginUser) {
		Optional<User> existingUser = userRepository.findByUsername(loginUser.getUsername());

		if (existingUser.isPresent() && existingUser.get().getPassword().equals(loginUser.getPassword())) {
			return "Login successful! Welcome " + existingUser.get().getName();
		} else {
			return "Invalid email or password!";
		}
	}

	@GetMapping("/login")
	public String login() {
		return "Who are you?";
	}
	
}
