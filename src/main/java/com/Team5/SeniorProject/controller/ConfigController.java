package com.Team5.SeniorProject.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;


@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "http://localhost:5173")
public class ConfigController {
    @Value("${spring.security.oauth2.client.registration.microsoft.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.microsoft.redirect-uri}")
    private String redirectUrl;

    @GetMapping("/microsoft")
    public Map<String, String> getMicrosoftConfig() {
        return Map.of(
            "clientId", clientId,
            "redirectUrl", redirectUrl
        );
    }
    
}
