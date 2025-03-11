package com.Team5.SeniorProject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.model.User;

@RestController
@RequestMapping("/api/SignupController")
@CrossOrigin(origins = "http://localhost:5173") 
public class SignupController {

	private static final List<User> users = new ArrayList<>();
	
	@PostMapping
	public String signup(@RequestBody User user) {
		// Simulate saving user (later replace with a database)
		users.add(user); 
		return "User signed up successfully!";
	}

	@GetMapping("/users")
	public List<User> getAllUsers() {
		
		return users;
	}

	@GetMapping("/login")
	public String login() {
		return "Who are you?";
	}
	
}
