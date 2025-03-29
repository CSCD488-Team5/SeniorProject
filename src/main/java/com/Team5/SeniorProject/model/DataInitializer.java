package com.Team5.SeniorProject.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.Team5.SeniorProject.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class DataInitializer implements CommandLineRunner {

	// Point to your local images using the classpath resource
    @Value("classpath:assets/events/event1/image.jpg")
    private Resource event1Image;

	@Value("classpath:assets/events/event2/image.jpg")
    private Resource event2Image;

	@Value("classpath:assets/events/event3/image.jpg")
    private Resource event3Image;

	@Value("classpath:assets/events/event4/image.jpg")
    private Resource event4Image;

	private final EventRepository eventRepository;

    public DataInitializer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

	@Override
    public void run(String... args) throws Exception {
         // ========== EVENT 1 ==========
		 byte[] imageBytes1 = FileCopyUtils.copyToByteArray(event1Image.getInputStream());
		 String base64Image1 = Base64.getEncoder().encodeToString(imageBytes1);
 
		 Event event1 = new Event();
		 event1.setTitle("Outdoor Basketball Showdown");
		 event1.setSubtitle("Feel the energy of the court");
		 event1.setContent("Join us for an epic outdoor basketball event!");
		 event1.setTime(LocalDateTime.parse("2023-07-15T15:00:00"));
		 event1.setLocation("Campus Basketball Court");
		 event1.setImageBase64(base64Image1);
 
		 eventRepository.save(event1);
 
		 // ========== EVENT 2 ==========
		 byte[] imageBytes2 = FileCopyUtils.copyToByteArray(event2Image.getInputStream());
		 String base64Image2 = Base64.getEncoder().encodeToString(imageBytes2);
 
		 Event event2 = new Event();
		 event2.setTitle("Graduation Commencement");
		 event2.setSubtitle("Celebrating Academic Achievements");
		 event2.setContent("Celebrate the graduation of our amazing class!");
		 event2.setTime(LocalDateTime.parse("2023-12-25T10:00:00"));
		 event2.setLocation("Main Auditorium");
		 event2.setImageBase64(base64Image2);
 
		 eventRepository.save(event2);
 
		 // ========== EVENT 3 ==========
		 byte[] imageBytes3 = FileCopyUtils.copyToByteArray(event3Image.getInputStream());
		 String base64Image3 = Base64.getEncoder().encodeToString(imageBytes3);
 
		 Event event3 = new Event();
		 event3.setTitle("Advanced Mathematics Workshop");
		 event3.setSubtitle("Sharpen your problem-solving skills");
		 event3.setContent("Dive into advanced math concepts in this interactive workshop.");
		 event3.setTime(LocalDateTime.parse("2023-11-05T09:00:00"));
		 event3.setLocation("Engineering Building");
		 event3.setImageBase64(base64Image3);
 
		 eventRepository.save(event3);
 
		 // ========== EVENT 4 ==========
		 byte[] imageBytes4 = FileCopyUtils.copyToByteArray(event4Image.getInputStream());
		 String base64Image4 = Base64.getEncoder().encodeToString(imageBytes4);
 
		 Event event4 = new Event();
		 event4.setTitle("Evening of Inspiration");
		 event4.setSubtitle("Ideas that spark change");
		 event4.setContent("Join a dynamic lineup of speakers and performers for an immersive evening of inspiration.");
		 event4.setTime(LocalDateTime.parse("2023-10-20T18:00:00"));
		 event4.setLocation("Performing Arts Center");
		 event4.setImageBase64(base64Image4);
 
		 eventRepository.save(event4);
    }
}
