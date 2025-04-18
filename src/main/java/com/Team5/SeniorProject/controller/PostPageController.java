package com.Team5.SeniorProject.controller;


import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.Post;
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

    @GetMapping("/{username}")
    public List<Event> getEventsByUserId(@PathVariable String username) {
      return eventRepository.findByUser_Username(username);
    }

    @PostMapping("/createPost")
    public ResponseEntity createPost(@RequestBody Event event) {
        eventRepository.save(event);
        return new ResponseEntity(event, HttpStatus.CREATED);
    }

    @PostMapping("/deletePost/{id}")//id - Represents the id attached to the post
    public ResponseEntity deletePost(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();//Return "Not Found" Error

        }else {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build();//The Deletion was done successfully
        }
    }
}