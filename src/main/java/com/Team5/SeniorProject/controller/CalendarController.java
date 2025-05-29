package com.Team5.SeniorProject.controller;

import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import com.Team5.SeniorProject.service.CalendarService;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow();
        
        boolean isConnected = calendarService.isConnected(user);
        return ResponseEntity.ok(Map.of("connected", isConnected));
    }

    @PostMapping("/oauth")
    public ResponseEntity<?> connect(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String code = body.get("code");
        if (code == null || code.isBlank()) return ResponseEntity.badRequest().body("Missing code");

        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow();

        calendarService.saveGoogleTokens(user, code);
        return ResponseEntity.ok("Calendar connected");
    }

    @PostMapping("/disconnect")
    public ResponseEntity<?> disconnect(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow();
        
        user.setGoogleAccessToken(null);
        user.setGoogleRefreshToken(null);
        user.setGoogleTokenExpiry(null);
        userRepository.save(user);
        
        return ResponseEntity.ok("Calendar disconnected");
    }
}