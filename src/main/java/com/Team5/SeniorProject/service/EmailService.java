package com.Team5.SeniorProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import com.Team5.SeniorProject.model.User;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendEventDeletionEmail(User user, String eventTitle) {
        String subject = "Posted Deleted from CampusHive";
        String message = "Hi " + user.getUsername() + ",\n\n" + 
                         "Your post titled \"" + eventTitle + "\" has been successfully deleted from CampusHive.\n\n" +
                         "Thanks, \nCampusHive Team";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

}