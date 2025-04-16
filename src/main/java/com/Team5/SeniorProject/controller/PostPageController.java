package com.Team5.SeniorProject.controller;


import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/PostPageController")
@CrossOrigin(origins = "https://localhost:5173")
public class PostPageController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public List<Event> getEventsByUserId(@PathVariable Long id) {
      return eventRepository.findByUser_Id(id);
    }


}