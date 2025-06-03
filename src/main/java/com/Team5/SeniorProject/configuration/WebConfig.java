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
								"https://campus-hive-51d7405b52bc.herokuapp.com/")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}
