package com.Team5.SeniorProject.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.service.UserService;

@Component
public class UserInitializer implements CommandLineRunner {

	private final UserService userService;

	public UserInitializer(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(String... args) throws Exception {
		// Only create the admin if it doesn't exist
		if (!userService.existsByUsername("Admin")) {
			User admin = new User();
			admin.setName("admin");
			admin.setEmail("admin@gmail.com");
			admin.setPassword("password"); // This will be encoded in the service
			admin.setUserName("admin");
			userService.signup(admin, "ADMIN");
			System.out.println("Admin account initialized.");
		}
	}

}
