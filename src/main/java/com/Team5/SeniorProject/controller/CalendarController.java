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

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CalendarController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Value("${google.calendar.client-id}")
    private String clientId;

    @Value("${google.calendar.client-secret}")
    private String clientSecret;

    @Value("${google.calendar.redirect-uri}")
    private String redirectUri;

    @PostMapping("/oauth")
    public ResponseEntity<?> handleGoogleOAuth(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String code = body.get("code");
        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().body("Missing authorization code");
        }

        // 🔐 Get JWT from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String jwt = authHeader.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔁 Exchange code for access + refresh tokens
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> googleRequest = new HttpEntity<>(params, headers);
        ResponseEntity<Map> googleResponse = restTemplate.postForEntity(
            "https://oauth2.googleapis.com/token",
            googleRequest,
            Map.class
        );

        if (!googleResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to exchange token with Google");
        }

        Map<String, Object> responseBody = googleResponse.getBody();
        String accessToken = (String) responseBody.get("access_token");
        String refreshToken = (String) responseBody.get("refresh_token");
        int expiresIn = (int) responseBody.get("expires_in");

        // 💾 Save token info in user
        user.setGoogleAccessToken(accessToken);
        user.setGoogleRefreshToken(refreshToken);
        user.setGoogleTokenExpiry(Instant.now().plusSeconds(expiresIn));
        userRepository.save(user);

        return ResponseEntity.ok("Google calendar connected");
    }
}
