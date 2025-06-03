package com.Team5.SeniorProject.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.UserRepository;

@RestController
@RequestMapping("/api/PostPageController")
public class PostPageController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

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
        String username = event.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() ->  new RuntimeException("User was not found for upload!"));
        event.setUser(user);
        eventRepository.save(event);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
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
