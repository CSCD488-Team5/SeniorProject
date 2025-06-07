package com.Team5.SeniorProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Team5.SeniorProject.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	Optional<Follow> findByFollower_IdAndFollowee_Id(Long followerId, Long followeeId);
	void deleteByFollower_IdAndFollowee_Id(Long followerId, Long followeeId);
	List<Follow> findByFollower_Id(Long followerId);
	List<Follow> findByFollowee_Id(Long followeeId);
	long countByFollowee_Username(String username);
}
