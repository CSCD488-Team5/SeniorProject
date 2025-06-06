package com.Team5.SeniorProject.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.jwt.JwtUtils;
import com.Team5.SeniorProject.jwt.LoginRequest;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.service.UserService;

@RestController
@RequestMapping("/api/auth") // Changed to match SecurityConfig
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
			// UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			// String jwt = jwtUtils.generateTokenFromUsername(userDetails);

			User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));
			String jwt = jwtUtils.generateToken(user);
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


	@PostMapping("/oauth/microsoft")
public ResponseEntity<?> microsoftSSO(@RequestBody Map<String, Object> profile) {
    // Extract email and name from the profile
    String email = (String) profile.get("email");
    String name = (String) profile.get("name");

    if (email == null || name == null) {
        return ResponseEntity.badRequest().body("Invalid profile data");
    }
	// Check if the user already exists
	User user = userService.findOrCreateMicrosoftUser(email, name);
    String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());

    return ResponseEntity.ok(Map.of("token", jwt));
}









}
