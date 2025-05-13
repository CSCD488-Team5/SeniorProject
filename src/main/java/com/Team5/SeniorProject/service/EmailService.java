package com.Team5.SeniorProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import com.Team5.SeniorProject.model.User;
import java.util.List;
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendEventDeletionEmail(List<User> users, String eventTitle) {
        String subject = "Event Deleted Notification - CampusHive";
        
        for (User user : users) {
            String message = "Hi " + user.getUsername() + ",\n\n" +
                             "The event titled \"" + eventTitle + "\" that you joined has been deleted.\n\n" + 
                             "Thanks,\nCampusHive Team";

        

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

}

}