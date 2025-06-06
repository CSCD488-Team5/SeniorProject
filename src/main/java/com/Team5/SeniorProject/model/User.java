package com.Team5.SeniorProject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = true)
	private String password;

	@Column(unique = true, nullable = false)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.USER; // default role

	@Column(nullable = false)
	private boolean enabled = true; // Added with default value

	@Column(length = 2048)
	private String googleAccessToken;

	@Column(length = 2048)
	private String googleRefreshToken;

	private Instant googleTokenExpiry;

	public User() {}

	public User(String name, String username, String email, String password, Role role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.username = username;
		this.role = role;
		this.enabled = true; // Default in constructor too
	}

	// Getters and Setters
	public Long getId() { return id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getUsername() { return username; }
	public void setUserName(String username) {this.username = username;}


	public Role getRole() {
		return this.role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }

	public String getGoogleAccessToken() {return googleAccessToken; }
	public void setGoogleAccessToken(String googleAccessToken) { this.googleAccessToken = googleAccessToken; }

	public String getGoogleRefreshToken() { return googleRefreshToken; }
	public void setGoogleRefreshToken(String googleRefreshToken) { this.googleRefreshToken = googleRefreshToken; }

	public Instant getGoogleTokenExpiry() { return googleTokenExpiry; }
	public void setGoogleTokenExpiry(Instant googleTokenExpiry) { this.googleTokenExpiry = googleTokenExpiry;}
}
