package com.Team5.SeniorProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.service.EventService;


@RestController
@RequestMapping("/api/events")
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

    @GetMapping("/{eventId}/is-joined")
    public ResponseEntity<Boolean> isUserJoinedEvent(@PathVariable Long eventId, @RequestParam String username) {
        try {
            boolean isJoined = eventService.isUserJoined(eventId, username);
            return ResponseEntity.ok(isJoined);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

}
