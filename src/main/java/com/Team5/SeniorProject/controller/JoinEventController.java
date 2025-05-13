package com.Team5.SeniorProject.controller;

import com.Team5.SeniorProject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class JoinEventController {

    private final EventService eventService;

    @Autowired
    public JoinEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{eventId}/join")
    public ResponseEntity<String> joinEvent(@PathVariable Long eventId, @RequestParam String username) {
        try {
            eventService.joinEvent(eventId, username);
            return ResponseEntity.ok("User successfully joined the event.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{eventId}/unjoin")
    public ResponseEntity<String> unjoinEvent(@PathVariable Long eventId, @RequestParam String username) {
        try {
            eventService.unjoinEvent(eventId, username);
            return ResponseEntity.ok("User successfully unjoined the event.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
