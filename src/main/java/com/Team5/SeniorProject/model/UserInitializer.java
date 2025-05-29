package com.Team5.SeniorProject.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.Team5.SeniorProject.service.UserService;

@Component
@Order(1)
public class UserInitializer implements CommandLineRunner {

	private final UserService userService;

	public UserInitializer(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			// Only create the admin if it doesn't exist
			if (!userService.existsByUsername("Admin")) {
				User admin = new User();
				admin.setName("admin");
				admin.setEmail("admin@gmail.com");
				admin.setPassword("password"); // This will be encoded in the service
				admin.setUserName("admin");
				userService.signup(admin, Role.ADMIN.name());
				System.out.println("Admin account initialized.");
			}

			// Create sample user1
			if (!userService.existsByUsername("alice")) {
				User user1 = new User();
				user1.setName("Alice");
				user1.setEmail("alice@example.com");
				user1.setPassword("password");
				user1.setUserName("alice");
				userService.signup(user1, Role.USER.name());
				System.out.println("User 'alice' created.");
			}

			// Create sample user2
			if (!userService.existsByUsername("bob")) {
				User user2 = new User();
				user2.setName("Bob");
				user2.setEmail("bob@example.com");
				user2.setPassword("password");
				user2.setUserName("bob");
				userService.signup(user2, Role.USER.name());
				System.out.println("User 'bob' created.");
			}
		} catch (Exception e) {
			System.err.println("UserInitializer failed: " + e.getMessage());
			e.printStackTrace();
		}

	}

}
