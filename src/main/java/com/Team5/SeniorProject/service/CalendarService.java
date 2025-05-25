package com.Team5.SeniorProject.service;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.JoinEvent;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.Instant;
import java.util.Map;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.JoinedEventRepository;
import java.util.List;;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final JoinedEventRepository joinedEventRepository;

    @Value("${google.calendar.client-id}")
    private String clientId;

    @Value("${google.calendar.client-secret}")
    private String clientSecret;

    @Value("${google.calendar.redirect-uri}")
    private String redirectUri;

    private RestTemplate rest = new RestTemplate();

    public void saveGoogleTokens(User user, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        // request to Google's token endpoint
        ResponseEntity<Map> response = rest.postForEntity(
                "https://oauth2.googleapis.com/token",
                new HttpEntity<>(body, headers),
                Map.class
        );

        // Save access + refresh tokens to the user
        Map<String, Object> tokens = response.getBody();
        user.setGoogleAccessToken((String) tokens.get("access_token"));
        user.setGoogleRefreshToken((String) tokens.get("refresh_token"));
        user.setGoogleTokenExpiry(Instant.now().plusSeconds((int) tokens.get("expires_in")));
        userRepository.save(user);

        syncPastEvents(user);
    }

    
    public String syncEvent(User user, Event event) {
        String token = getAccessToken(user);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        ZonedDateTime start = event.getTime().atZone(ZoneId.of("America/Los_Angeles"));
        ZonedDateTime end = start.plusHours(1);
        String startTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(start);
        String endTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(end);
    
        Map<String, Object> payload = Map.of(
            "summary", event.getTitle(),
            "description", event.getDescription(),
            "start", Map.of("dateTime", startTime, "timeZone", "America/Los_Angeles"),
            "end", Map.of("dateTime", endTime, "timeZone", "America/Los_Angeles")
        );
    
        ResponseEntity<Map> response = rest.postForEntity(
            "https://www.googleapis.com/calendar/v3/calendars/primary/events",
            new HttpEntity<>(payload, headers),
            Map.class
        );
    
        Map<String, Object> body = response.getBody();
        return (String) body.get("id"); // Google’s event ID
    }
    

    
    public boolean isConnected(User user) {
        return user.getGoogleAccessToken() != null && user.getGoogleRefreshToken() != null;
    }

    
    public String getAccessToken(User user) {

        if (user.getGoogleAccessToken() != null && Instant.now().isBefore(user.getGoogleTokenExpiry())) {
            return user.getGoogleAccessToken();
        }

        // Refresh the token if expired
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", user.getGoogleRefreshToken());
        body.add("grant_type", "refresh_token");

        // Call Google's token endpoint to refresh the access token
        ResponseEntity<Map> response = rest.postForEntity(
                "https://oauth2.googleapis.com/token",
                new HttpEntity<>(body, headers),
                Map.class
        );

        // Update and save the new token
        Map<String, Object> data = response.getBody();
        String token = (String) data.get("access_token");
        user.setGoogleAccessToken(token);
        user.setGoogleTokenExpiry(Instant.now().plusSeconds((int) data.get("expires_in")));
        userRepository.save(user);

        return token;
    }

    public void deleteGoogleCalendarEvent(User user, String calendarEventId) {
    String token = getAccessToken(user);
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    HttpEntity<Void> request = new HttpEntity<>(headers);

    try {
        rest.exchange(
            "https://www.googleapis.com/calendar/v3/calendars/primary/events/" + calendarEventId,
            HttpMethod.DELETE,
            request,
            Void.class
        );
    } catch (HttpClientErrorException e) {
        System.err.println("Failed to delete event from calendar: " + e.getMessage());
    }
}

    public void deleteIfConnected(User user, String calendarEventId) {
        if (isConnected(user) && calendarEventId != null && !calendarEventId.isBlank()) {
            deleteGoogleCalendarEvent(user, calendarEventId);
        }
    }

    public void syncPastEvents(User user) {
    // Sync created events
    List<Event> createdEvents = eventRepository.findByUser_Id(user.getId());
    for (Event event : createdEvents) {
        if (event.getCreatorCalendarEventId() == null) {
            String id = syncEvent(user, event);
            event.setCreatorCalendarEventId(id);
            eventRepository.save(event);
        }
    }

    // Sync joined events
    List<JoinEvent> joinedEvents = joinedEventRepository.findByUser_Username(user.getUsername());
    for (JoinEvent join : joinedEvents) {
        if (join.getGoogleCalendarEventId() == null) {
            String id = syncEvent(user, join.getEvent());
            join.setGoogleCalendarEventId(id);
            joinedEventRepository.save(join);
        }
    }
}



}
