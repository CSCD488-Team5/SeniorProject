package com.Team5.SeniorProject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class CampusHiveApplication {

	// Twilio Credentials
	@Value("${twilio.account.sid}")
	private String ACCOUNT_SID;
	@Value("${twilio.auth.token}")
	private String AUTH_ID;

	// Initializing the Twilio Java Library with our credentials
	@PostConstruct
	public void initTwilio() {
		Twilio.init(ACCOUNT_SID, AUTH_ID);
	}


	public static void main(String[] args) {
		SpringApplication.run(CampusHiveApplication.class, args);
	}

}
