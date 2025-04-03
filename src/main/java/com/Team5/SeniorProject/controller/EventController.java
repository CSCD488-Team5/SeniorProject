package com.Team5.SeniorProject.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.repository.EventRepository;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {
	
	@Autowired
	private EventRepository eventRepository;

	@GetMapping
	public List<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	// Endpoint for creating an event with an image
    @PostMapping("/upload")
    public ResponseEntity<Event> createEventWithImage(
            // Category
            // Author 
            @RequestParam("title") String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("content") String content,
            // DateTime
            @RequestParam("time") String time, // ISO-8601 format, e.g., 2023-12-25T15:00:00
            @RequestParam("location") String location,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            Event event = new Event();
            event.setTitle(title);
            event.setSubtitle(subtitle);
            event.setContent(content);
            event.setTime(LocalDateTime.parse(time));
            event.setLocation(location);
            event.setImageBase64(Base64.getEncoder().encodeToString(imageFile.getBytes()));
            Event savedEvent = eventRepository.save(event);
            return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	// Optional: endpoint to get an event by id (you can add more endpoints as needed)
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
