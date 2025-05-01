package com.Team5.SeniorProject.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Team5.SeniorProject.model.Follow;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.FollowRepository;
import com.Team5.SeniorProject.repository.UserRepository;

@Service
public class FollowService {
	private final FollowRepository followRepository;
	private final UserRepository userRepository;

	public FollowService(FollowRepository followRepository, UserRepository userRepository) {
		this.followRepository = followRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public void follow(Long followerId, Long followeeId) {
		if (followRepository.findByFollower_IdAndFollowee_Id(followerId, followeeId).isEmpty()) {
			User follower = userRepository.findById(followerId).orElseThrow();
			User followee = userRepository.findById(followeeId).orElseThrow();
			followRepository.save(new Follow(follower, followee));
		}
	}

	@Transactional
	public void unfollow(Long followerId, Long followeeId) {
		followRepository.deleteByFollower_IdAndFollowee_Id(followerId, followeeId);
	}

	public boolean isFollowing(Long followerId, Long followeeId) {
		return followRepository.findByFollower_IdAndFollowee_Id(followerId, followeeId).isPresent();
	}

	public List<User> listFollowers(Long userId) {
		return followRepository.findByFollowee_Id(userId).stream()
				.map(Follow::getFollower).toList();
	}

}
