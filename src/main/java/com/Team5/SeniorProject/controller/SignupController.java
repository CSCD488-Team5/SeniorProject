package com.Team5.SeniorProject.controller;

import com.Team5.SeniorProject.jwt.JwtUtils;
import com.Team5.SeniorProject.jwt.LoginRequest;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.Team5.SeniorProject.service.UserService;

@RestController
@RequestMapping("/api/auth") // Changed to match SecurityConfig
@CrossOrigin(origins = "http://localhost:5173")
public class SignupController {

    private final UserService userService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	@Autowired
	public SignupController(UserRepository userRepository, PasswordEncoder passwordEncoder,
							AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody User user) {
		try {
            // For regular signup, assign USER role
            userService.signup(user, "USER");
            return ResponseEntity.ok("User signed up successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
		System.out.println("Login attempt - Username: " + loginRequest.getUsername() + ", Password: " + loginRequest.getPassword());
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String jwt = jwtUtils.generateTokenFromUsername(userDetails);
			System.out.println("Login successful - JWT: " + jwt);
			return ResponseEntity.ok(jwt);
		} catch (AuthenticationException e) {
			System.out.println("Authentication failed: " + e.getMessage());
			throw e; // Let AuthEntryPointJwt handle it
		}
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userRepository.findAll());
	}
}
