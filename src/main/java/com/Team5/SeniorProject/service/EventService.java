package com.Team5.SeniorProject.service;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.JoinEvent;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.JoinedEventRepository;
import com.Team5.SeniorProject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Team5.SeniorProject.service.CalendarService;
import java.time.LocalDateTime;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final JoinedEventRepository joinedEventRepository;
    private final CalendarService calendarService;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository,
                        JoinedEventRepository joinedEventRepository,
                        CalendarService calendarService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.joinedEventRepository = joinedEventRepository;
        this.calendarService = calendarService;
    }

    @Transactional
    public void joinEvent(Long eventId, String username) throws Exception {
        if (joinedEventRepository.existsByEvent_IdAndUser_Username(eventId, username)) {
            throw new Exception("User has already joined this event.");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new Exception("Event not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));

        JoinEvent joinEvent = new JoinEvent();
        joinEvent.setEvent(event);
        joinEvent.setUser(user);
        joinEvent.setJoined(LocalDateTime.now());

        joinedEventRepository.save(joinEvent);

        if (user.getGoogleAccessToken() != null && user.getGoogleRefreshToken() != null) {
            try {
                calendarService.createGoogleCalendarEvent(user, event);
            } catch (Exception e) {
                System.err.println("Google Calendar sync failed: " + e.getMessage());
                // Optionally: Log it but don't block the join
            }
        }

    }

    @Transactional
    public void unjoinEvent(Long eventId, String username) throws Exception {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new Exception("Event not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));

        JoinEvent joinEvent = joinedEventRepository
                .findByEvent_Id(eventId).stream()
                .filter(e -> e.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new Exception("User has not joined this event."));

        joinedEventRepository.delete(joinEvent);
    }

    public boolean isUserJoined(Long eventId, String username) {
        return joinedEventRepository.existsByEvent_IdAndUser_Username(eventId, username);
    }
}
