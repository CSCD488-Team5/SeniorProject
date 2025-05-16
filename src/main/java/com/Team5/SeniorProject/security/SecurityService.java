package com.Team5.SeniorProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.repository.EventRepository;

@Service("securityService")
public class SecurityService {
	
	private final EventRepository eventRepository;

	@Autowired
	public SecurityService(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	/**
     * Returns true if the currently authenticated user is the creator (owner)
     * of the event with the given ID.
     */
	public boolean isEventOwner(Long eventId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = auth.getName();
		return eventRepository.findById(eventId)
			.map(Event::getUser)
			.map(u -> u.getUsername().equals(currentUsername))
			.orElse(false);
	}
}
