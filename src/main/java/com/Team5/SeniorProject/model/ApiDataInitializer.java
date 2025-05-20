package com.Team5.SeniorProject.model;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Value("classpath:assets/events/event1/image.jpg")
    private Resource event1Image;

    @Value("classpath:assets/events/event2/image.jpg")
    private Resource event2Image;

    @Value("classpath:assets/events/event3/image.jpg")
    private Resource event3Image;

    @Value("classpath:assets/events/event4/image.jpg")
    private Resource event4Image;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String uploadUrl = "http://localhost:80/api/events/upload";

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
            uploadEvent("Outdoor Basketball Showdown", "SPORTS", "Join us for an epic outdoor basketball event!",
                    "2023-07-15T15:00:00", "Campus Basketball Court", event1Image, "bob");

            uploadEvent("Graduation Commencement", "BUSINESS", "Celebrate the graduation of our amazing class!",
                    "2023-12-25T10:00:00", "Main Auditorium", event2Image, "bob");

            uploadEvent("Advanced Mathematics Workshop", "MATH", "Dive into advanced math concepts in this interactive workshop.",
                    "2023-11-05T09:00:00", "Engineering Building", event3Image, "alice");

            uploadEvent("Evening of Inspiration",  "ARTS", "Join a dynamic lineup of speakers and performers for an immersive evening of inspiration.",
                    "2023-10-20T18:00:00", "Performing Arts Center", event4Image, "alice");

        } catch (IOException e) {
            System.err.println("Failed to upload one or more events: " + e.getMessage());
        }
	}

	 private void uploadEvent(String title, String category, String description, String time, String location, Resource imageResource, String username) throws IOException {

        ByteArrayResource fileAsResource = new ByteArrayResource(imageResource.getInputStream().readAllBytes()) {
            @Override
            public String getFilename() {
                return "upload.jpg"; // default file name
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("title", title);
        body.add("category", category);
        body.add("description", description);
        body.add("time", time);
        body.add("location", location);
        body.add("image", fileAsResource);
        body.add("username", username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);
        System.out.println("Uploaded event: " + title + " => " + response.getStatusCode());
    }
}
