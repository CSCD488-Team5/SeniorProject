package com.Team5.SeniorProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Team5.SeniorProject.model.User;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
    }

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
            emailSender.send(email);
        }
    }

    public void sendEventDeletionEmail(List<User> users, String eventTitle, String reason) {
        String subject = "Event Deleted by Admin - CampusHive";

        for (User user : users) {
            String message = "Hi " + user.getUsername() + ",\n\n" +
                            "The event titled \"" + eventTitle + "\" was deleted by an administrator.\n\n" +
                            "Reason: " + reason + "\n\n" +
                            "Thanks,\nCampusHive Team";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(user.getEmail());
            email.setSubject(subject);
            email.setText(message);
            emailSender.send(email);
        }
    }

    public void sendNewEventNotification(List<User> followers, String creatorName, String eventTitle) {
        String subject = "New Event by " + creatorName + " - CampusHive";

        for (User follower : followers) {
            String message = "Hi " + follower.getUsername() + ",\n\n" +
                    creatorName + " has posted a new event: \"" + eventTitle + "\".\n\n" +
                    "Check it out on CampusHive!\n\nThanks,\nCampusHive Team";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(follower.getEmail());
            email.setSubject(subject);
            email.setText(message);
            emailSender.send(email);
        }
    }

    public void sendCommentDeletionEmail(User recipient, String eventTitle, String commentContent, String reason) {
        String subject = "Your comment has been deleted by a moderator";
        
        String content = String.format(
        "Hi %s,\n\n" +
        "Your comment on the event \"%s\" has been deleted by a moderator.\n\n" +
        "Reason for deletion:\n%s\n\n" +
        "Original comment:\n\"%s\"\n\n" +
        "If you believe this was a mistake, feel free to contact support.\n\n" +
        "Best regards,\nThe Eastern Classes Team",
        recipient.getUsername(),
        eventTitle,
        reason,
        commentContent
    );

        sendEmail(recipient.getEmail(), subject, content);
    }

}
