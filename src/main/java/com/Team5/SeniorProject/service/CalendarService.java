package com.Team5.SeniorProject.service;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalendarService {

    private final UserRepository userRepository;
    @Value("${google.calendar.client-id}")
    private String clientId;

    @Value("${google.calendar.client-secret}")
    private String clientSecret;

    

    public CalendarService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getValidAccessToken(User user) {
        if (user.getGoogleAccessToken() != null &&
            user.getGoogleTokenExpiry() != null &&
            Instant.now().isBefore(user.getGoogleTokenExpiry())) {
            return user.getGoogleAccessToken();
        }

        // Refresh token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("refresh_token", user.getGoogleRefreshToken());
        params.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://oauth2.googleapis.com/token",
            request,
            Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to refresh access token");
        }

        Map<String, Object> responseBody = response.getBody();
        String newToken = (String) responseBody.get("access_token");
        int expiresIn = (Integer) responseBody.get("expires_in");

        user.setGoogleAccessToken(newToken);
        user.setGoogleTokenExpiry(Instant.now().plusSeconds(expiresIn));
        userRepository.save(user);

        return newToken;
    }

    public void createGoogleCalendarEvent(User user, Event event) {
        String token = getValidAccessToken(user);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Format the event time to RFC3339 format
        ZonedDateTime zonedStart = event.getTime().atZone(ZoneId.of("America/Los_Angeles"));
    ZonedDateTime zonedEnd = event.getTime().plusHours(1).atZone(ZoneId.of("America/Los_Angeles"));
    String startTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedStart);
    String endTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(zonedEnd);
    
        Map<String, Object> eventPayload = new HashMap<>();
        eventPayload.put("summary", event.getTitle());
        eventPayload.put("description", event.getDescription());
    
        eventPayload.put("start", Map.of(
            "dateTime", startTime,
            "timeZone", "America/Los_Angeles"
        ));
    
        eventPayload.put("end", Map.of(
            "dateTime", endTime,
            "timeZone", "America/Los_Angeles"
        ));
    
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(eventPayload, headers);
        new RestTemplate().postForEntity(
            "https://www.googleapis.com/calendar/v3/calendars/primary/events",
            request,
            String.class
        );
    }
    
}
