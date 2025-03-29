package com.Team5.SeniorProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.repository.EventRepository;

@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	private EventRepository eventRepository;

	@GetMapping
	public List<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	@PostMapping
	public Event createEvent(@RequestBody Event event) {
		return eventRepository.save(event);
	}

	@GetMapping("/{id}")
	public Event getEventById(@PathVariable Long id) {
		return eventRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Event not found"));
	}

}
