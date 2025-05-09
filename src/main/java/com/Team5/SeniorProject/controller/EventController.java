package com.Team5.SeniorProject.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import com.Team5.SeniorProject.service.EmailService;


@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/events/";
	
	@Autowired
	private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

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
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            // DateTime
            @RequestParam("time") String time, // ISO-8601 format, e.g., 2023-12-25T15:00:00
            @RequestParam("location") String location,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("username") String username) {
        try {

            User user = userRepository.findByUsername(username).orElseThrow(() ->  new RuntimeException("User was not found for upload!"));

            // Step 1: Save image to /static/images/events/
            String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            java.nio.file.Path filePath = java.nio.file.Paths.get(UPLOAD_DIR, filename);
            java.nio.file.Files.createDirectories(filePath.getParent());
            java.nio.file.Files.write(filePath, imageFile.getBytes());

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
            
            Event savedEvent = eventRepository.save(event);
            return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	// Optional: endpoint to get an event by id (you can add more endpoints as needed)
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    //EndPoint: Find events by username.
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Event>> getEventByUsername(@PathVariable String username){
        List<Event> events = eventRepository.findByUser_Username(username);
        if (events.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    //EndPoint: Updates the events
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable long id,
    @RequestParam("title") String title,
    @RequestParam("subtitle") String subtitle,
    @RequestParam("category") String category,
    @RequestParam("description") String description,
    @RequestParam("time") String time, // ISO-8601 format, e.g., 2023-12-25T15:00:00
    @RequestParam("location") String location,
    @RequestParam(value = "image", required = false) MultipartFile imageFile,
    @RequestParam("username") String username) 
    {
        Event event = eventRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User was not found!"));

        //Set all attributes of the event, except Image
        event.setTitle(title);
        event.setSubtitle(subtitle);
        event.setCategory(category);
        event.setDescription(description);
        event.setTime(LocalDateTime.parse(time));
        event.setLocation(location);
        event.setUser(user);

        if(imageFile!= null && !imageFile.isEmpty()){
        try {
        // Step 1: Save image to /static/images/events/
        String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        java.nio.file.Path filePath = java.nio.file.Paths.get(UPLOAD_DIR, filename);
        java.nio.file.Files.createDirectories(filePath.getParent());
        java.nio.file.Files.write(filePath, imageFile.getBytes());

        // Step 2: Build relative URL for serving
        String imageUrl = "/images/events/" + filename;

        event.setImageUrl(imageUrl);

        } catch(IOException e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    Event updatedEvent = eventRepository.save(event);
    return new ResponseEntity<>(updatedEvent, HttpStatus.OK);

    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        if(!eventRepository.existsById(id)){
            return ResponseEntity.noContent().build(); //If event does not exist.
        }
        Event event = eventRepository.findById(id).orElse(null);
        if (event.getUser() != null) {
            User user = event.getUser();
            String title = event.getTitle();
            eventRepository.deleteById(id);
            emailService.sendEventDeletionEmail(user, title);
            }

        return ResponseEntity.noContent().build();// Deletes the event.
    }

    
    
    

}
