package com.Team5.SeniorProject.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.service.FollowService;

@RestController
@RequestMapping("/api/users")
public class FollowController {
	
	private final FollowService followService;
	private final UserRepository userRepository;

	public FollowController(FollowService followService, UserRepository userRepository) {
		this.followService = followService;
		this.userRepository = userRepository;
	}

	@PostMapping("/{id}/follow")
	public ResponseEntity<Void> follow(@PathVariable Long id, Principal principal) {
		Long me = userRepository.findByUsername(principal.getName()).orElseThrow().getId();
		followService.follow(me, id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/unfollow") 
	public ResponseEntity<Void> unfollow(@PathVariable Long id, Principal principal) {
		Long me = userRepository.findByUsername(principal.getName()).orElseThrow().getId();
		followService.unfollow(me, id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/is-following")
	public boolean isFollowing(@PathVariable Long id, Principal principal) {
		Long me = userRepository.findByUsername(principal.getName()).orElseThrow().getId();
		return followService.isFollowing(me, id);
	}

	@GetMapping("/{id}/followers")
	public List<User> getFollowers(@PathVariable Long id) {
		return followService.listFollowers(id);
	}

	@GetMapping("/{id}/following")
	public List<User> getFollowing(@PathVariable Long id) {
		return followService.listFollowing(id);
	}
}
