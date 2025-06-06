package com.Team5.SeniorProject.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.EventCategory;
import com.Team5.SeniorProject.model.JoinEvent;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.FollowRepository;
import com.Team5.SeniorProject.repository.JoinedEventRepository;
import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.service.CalendarService;
import com.Team5.SeniorProject.service.EmailService;

@RestController
@RequestMapping("/api/events")
public class EventController {


    // inject from application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JoinedEventRepository joinedEventRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private CalendarService calendarService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Endpoint for creating an event with an image
    @PostMapping("/upload")
    public ResponseEntity<Event> createEventWithImage(
            // Category
            // Author cle
            @RequestParam("title") String title,
            @RequestParam("category") String categoryStr,
            @RequestParam("description") String description,
            // DateTime
            @RequestParam("time") String time, // ISO-8601 format, e.g., 2023-12-25T15:00:00
            @RequestParam("location") String location,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("username") String username) {
        try {

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User was not found for upload!"));

            EventCategory category;
            try {
                category = EventCategory.valueOf(categoryStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Step 1: Save image to /static/images/events/
            String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, imageFile.getBytes());

            // Step 2: Build relative URL for serving
            String imageUrl = "/images/events/" + filename;

            // Step 3: Save event with imageUrl
            Event event = new Event();
            event.setTitle(title);
            event.setCategory(category);
            event.setDescription(description);
            event.setTime(LocalDateTime.parse(time));
            event.setLocation(location);
            event.setImageUrl(imageUrl);
            event.setUser(user);

            if (calendarService.isConnected(user)) {
                String calendarEventId = calendarService.syncEvent(user, event);
                event.setCreatorCalendarEventId(calendarEventId);
            }

            Event savedEvent = eventRepository.save(event);
            List<User> followers = followRepository.findByFollowee_Id(user.getId())
                    .stream()
                    .map(follow -> follow.getFollower())
                    .toList();
            emailService.sendNewEventNotification(followers, user.getUsername(), event.getTitle());
            return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Optional: endpoint to get an event by id (you can add more endpoints as
    // needed)
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // EndPoint: Find events by username.
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Event>> getEventByUsername(@PathVariable String username) {
        List<Event> events = eventRepository.findByUser_Username(username);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // EndPoint: Updates the events
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable long id,
            @RequestParam("title") String title,
            @RequestParam("category") String categoryStr,
            @RequestParam("description") String description,
            @RequestParam("time") String time, // ISO-8601 format, e.g., 2023-12-25T15:00:00
            @RequestParam("location") String location,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("username") String username) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User was not found!"));

        EventCategory category;
        try {
            category = EventCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Set all attributes of the event, except Image
        event.setTitle(title);
        event.setCategory(category);
        event.setDescription(description);
        event.setTime(LocalDateTime.parse(time));
        event.setLocation(location);
        event.setUser(user);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Step 1: Save image to /static/images/events/
                String originalFilename = imageFile.getOriginalFilename();
                if (originalFilename == null) {
                    originalFilename = "default_image.png"; // Fallback if filename is null
                }
                String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); // Sanitize filename

                String filename = System.currentTimeMillis() + "_" + safeFilename;

                Path filePath = Paths.get(uploadDir, filename);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());

                // Step 2: Build relative URL for serving
                String imageUrl = "/images/events/" + filename;

                event.setImageUrl(imageUrl);

            } catch (IOException e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        Event updatedEvent = eventRepository.save(event);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.isEventOwner(#id)")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.noContent().build(); // If event does not exist.
        }

        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

        List<JoinEvent> joinEvents = joinedEventRepository.findByEvent_Id(id);
        List<User> participants = new ArrayList<>(
                joinEvents.stream()
                        .map(JoinEvent::getUser)
                        .distinct()
                        .toList() // still immutable, but we immediately copy
        );

        User creator = event.getUser();
        if (!participants.contains(creator)) {
            participants.add(creator);
        }

        joinEvents.forEach(join -> 
            calendarService.deleteIfConnected(join.getUser(), join.getGoogleCalendarEventId())
        );

        calendarService.deleteIfConnected(event.getUser(), event.getCreatorCalendarEventId());

        joinedEventRepository.deleteAll(joinEvents);
        eventRepository.deleteById(id);

        emailService.sendEventDeletionEmail(participants, event.getTitle());
        return ResponseEntity.noContent().build();// Deletes the event.
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getUniqueCategories() {
        List<String> categories = Arrays.stream(EventCategory.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/getAllEventsForUser/{username}")
    public ResponseEntity<List<Event>> getAllEventsForUser(@PathVariable String username) {
        // 1. Get all events
        List<Event> allEvents = eventRepository.findAll();

        // 2. Get the list of event IDs the user joined
        List<JoinEvent> joinedEntries = joinedEventRepository.findByUser_Username(username);
        Set<Long> joinedEventsIds = joinedEntries.stream()
                .map(join -> join.getEvent().getId())
                .collect(Collectors.toSet());

        // 3. Mark events as joined
        for (Event event : allEvents) {
            if (joinedEventsIds.contains(event.getId())) {
                event.setJoined(true);
            }
        }

        return ResponseEntity.ok(allEvents);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-event-admin/{id}")
    public ResponseEntity<?> deleteEventAsAdmin(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {

        if (!eventRepository.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found.");

        String reason = body != null ? body.get("reason") : null;
        if (reason == null || reason.trim().isEmpty()) return ResponseEntity.badRequest().body("Deletion reason is required.");

        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found.");

        List<JoinEvent> joinEvents = joinedEventRepository.findByEvent_Id(id);
        List<User> participants = joinEvents.stream()
                .map(JoinEvent::getUser)
                .distinct()
                .collect(Collectors.toList());
        
        User creator = event.getUser();
        if (!participants.contains(creator)) participants.add(creator);

        joinedEventRepository.deleteAll(joinEvents);
        eventRepository.deleteById(id);

        emailService.sendEventDeletionEmail(participants, event.getTitle(), reason);

        return ResponseEntity.ok("Event deleted by admin with reason: " + reason);
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws MalformedURLException, IOException {
    Path path = Paths.get(uploadDir).resolve(filename);
    Resource img = new UrlResource(path.toUri());
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
        .body(img);
}

}
