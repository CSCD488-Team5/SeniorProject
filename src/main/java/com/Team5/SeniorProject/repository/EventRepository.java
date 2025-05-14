package com.Team5.SeniorProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Team5.SeniorProject.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByUser_Id(Long userId);
	List<Event> findByUser_Username(String username);// Will this work idk lol

	@Query("SELECT DISTINCT e.category FROM Event e")
	List<String> findDistinctCategories();
}
