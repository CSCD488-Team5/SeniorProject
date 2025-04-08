package com.Team5.SeniorProject.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User signup(User user, String role) throws Exception {
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new Exception("Error: Username already exists!");
		}
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Error: Email already exists!");
        }
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(role);
		return userRepository.save(user);
	}

	public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

	
}
