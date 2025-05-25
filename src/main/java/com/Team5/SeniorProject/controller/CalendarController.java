package com.Team5.SeniorProject.controller;

import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
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
}