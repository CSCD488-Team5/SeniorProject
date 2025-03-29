package com.Team5.SeniorProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Team5.SeniorProject.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	
}
