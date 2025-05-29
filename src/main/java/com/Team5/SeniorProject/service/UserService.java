package com.Team5.SeniorProject.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Team5.SeniorProject.model.Role;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.JoinedEventRepository;
import com.Team5.SeniorProject.repository.UserRepository;
import jakarta.transaction.Transactional;
import com.Team5.SeniorProject.repository.CommentRepository;

@Service
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JoinedEventRepository joinedEventRepository;
	private final EventRepository eventRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JoinedEventRepository joinedEventRepository, EventRepository eventRepository, CommentRepository commentRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.joinedEventRepository = joinedEventRepository;
		this.eventRepository = eventRepository;
	}

	// public User signup(User user, String role) throws Exception {
	// 	if (userRepository.findByUsername(user.getUsername()).isPresent()) {
	// 		throw new Exception("Error: Username already exists!");
	// 	}
	// 	if (userRepository.findByEmail(user.getEmail()).isPresent()) {
    //         throw new Exception("Error: Email already exists!");
    //     }
	// 	user.setPassword(passwordEncoder.encode(user.getPassword()));
	// 	user.setRole(Role.valueOf(role));
	// 	return userRepository.save(user);
	// }

	public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

	
	public void deleteUserByUsername(String username) {
		userRepository.findByUsername(username).ifPresent(user -> {
			joinedEventRepository.deleteAll(joinedEventRepository.findByUser_Username(username));
			eventRepository.deleteAll(eventRepository.findByUser_Username(username));
			userRepository.delete(user);
		});
	}

	public User signup(User user, String role) throws Exception {
		if (user.getUsername() != null && userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new Exception("Error: Username already exists!");
		}
		if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new Exception("Error: Email already exists!");
		}
	
		// Only encode password if it's non-null and not empty (i.e., not an SSO user)
		if (user.getPassword() != null && !user.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
	
		user.setRole(Role.valueOf(role));
		return userRepository.save(user);
	}

	public User findOrCreateMicrosoftUser(String email, String name) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setUserName(email.split("@")[0]);
			newUser.setName(name);
			newUser.setPassword("");
			newUser.setRole(Role.USER);
			newUser.setEnabled(true);
			return userRepository.save(newUser);
		});
 	}
	
}
