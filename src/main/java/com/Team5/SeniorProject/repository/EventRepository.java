package com.Team5.SeniorProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Team5.SeniorProject.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByUser_Id(Long userId);
	List<Event> findByUser_Username(String username);// Will this work idk lol
}
