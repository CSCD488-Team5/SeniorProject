package com.Team5.SeniorProject.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// Allow CORS for static images
		registry.addMapping("/**") // allow all endpoints
				.allowedOrigins(
					"http://localhost:5173", 
								"fascinating-hamster-cb5840.netlify.app")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
