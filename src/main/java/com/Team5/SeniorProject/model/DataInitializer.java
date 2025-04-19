package com.Team5.SeniorProject.model;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Component;


@Component
public class DataInitializer {


	// // Point to your local images using the classpath resource
    // @Value("classpath:assets/events/event1/image.jpg")
    // private Resource event1Image;


	// @Value("classpath:assets/events/event2/image.jpg")
    // private Resource event2Image;

	// @Value("classpath:assets/events/event3/image.jpg")
    // private Resource event3Image;

	// @Value("classpath:assets/events/event4/image.jpg")
    // private Resource event4Image;

	// private final EventRepository eventRepository;

	// private static final String OUTPUT_DIR = "src/main/resources/static/images/events";

    // public DataInitializer(EventRepository eventRepository) {
    //     this.eventRepository = eventRepository;
    // }

	// @Override
    // public void run(String... args) throws Exception {
    //     createEvent("event1.jpg", event1Image, "Outdoor Basketball Showdown", "Feel the energy of the court",
    //             "Join us for an epic outdoor basketball event!", "2023-07-15T15:00:00", "Campus Basketball Court");

    //     createEvent("event2.jpg", event2Image, "Graduation Commencement", "Celebrating Academic Achievements",
    //             "Celebrate the graduation of our amazing class!", "2023-12-25T10:00:00", "Main Auditorium");

    //     createEvent("event3.jpg", event3Image, "Advanced Mathematics Workshop", "Sharpen your problem-solving skills",
    //             "Dive into advanced math concepts in this interactive workshop.", "2023-11-05T09:00:00", "Engineering Building");

    //     createEvent("event4.jpg", event4Image, "Evening of Inspiration", "Ideas that spark change",
    //             "Join a dynamic lineup of speakers and performers for an immersive evening of inspiration.",
    //             "2023-10-20T18:00:00", "Performing Arts Center");
    // }

	// private void createEvent(String filename, Resource image, String title, String subtitle,
    //                          String content, String dateTime, String location) throws IOException {

    //     // Ensure output dir exists
    //     File outputDir = new File(OUTPUT_DIR);
    //     if (!outputDir.exists()) outputDir.mkdirs();

    //     // Copy image from classpath to static folder
    //     File targetFile = new File(OUTPUT_DIR + filename);
    //     try (FileOutputStream fos = new FileOutputStream(targetFile)) {
    //         FileCopyUtils.copy(image.getInputStream(), fos);
    //     }

    //     // Create and save the event
    //     Event event = new Event();
    //     event.setTitle(title);
    //     event.setSubtitle(subtitle);
    //     event.setContent(content);
    //     event.setTime(LocalDateTime.parse(dateTime));
    //     event.setLocation(location);
    //     event.setImageUrl("/images/events/" + filename); // just store relative path

    //     eventRepository.save(event);
    // }
}
