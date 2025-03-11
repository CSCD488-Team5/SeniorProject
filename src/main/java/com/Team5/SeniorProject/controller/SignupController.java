package com.Team5.SeniorProject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/SignupController")
public class SignupController {

	@GetMapping("/login")
	public String login() {
		return "Who are you?";
	}
	
}
