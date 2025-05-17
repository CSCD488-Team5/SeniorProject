package com.Team5.SeniorProject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Team5.SeniorProject.model.TwilioRequest;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@RestController
@RequestMapping("/sms")
public class SmsController {
	@PostMapping("/send")
	public ResponseEntity<String> sendMessag(@RequestBody TwilioRequest twilioRequest) {

		// Check if RequestBody has valid data or NOT
		if (twilioRequest == null || twilioRequest.getFromPhoneNumber() == null
			|| twilioRequest.getToPhoneNumber() == null 
			|| twilioRequest.getMessage() == null) {
				return ResponseEntity.badRequest().body("Invalid request data");
		}

		// Extract Request Data
		String fromNumber = twilioRequest.getFromPhoneNumber();
		String toNumber = twilioRequest.getToPhoneNumber();
		String msg = twilioRequest.getMessage();

		// Create Message to be sent
		Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber), msg).create();
		
		return ResponseEntity.ok("SMS sent Succesfully !");
	}
	
}
