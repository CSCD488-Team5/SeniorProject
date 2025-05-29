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

    public void sendCommentDeletionEmail(User recipient, String eventTitle, String commentContent, String reason, String adminUsername) {
        String subject = "Your comment has been deleted by a moderator";
        
        String content = String.format("""
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f8f9fa; padding: 20px; border-radius: 5px; margin-bottom: 20px; }
                    .content { background-color: #ffffff; padding: 20px; border: 1px solid #dee2e6; border-radius: 5px; }
                    .reason { background-color: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h2>Comment Deleted by Moderator</h2>
                </div>
                
                <div class="content">
                    <p>Dear %s,</p>
                    
                    <p>A comment you made on the event "%s" has been deleted by moderator %s.</p>
                    
                    <div class="reason">
                        <strong>Reason for deletion:</strong>
                        <p>%s</p>
                    </div>
                    
                    <p>The deleted comment content was:</p>
                    <blockquote>%s</blockquote>
                    
                    <p>If you believe this action was taken in error, please contact our support team.</p>
                    
                    <p>Best regards,<br>The Eastern Classes Team</p>
                </div>
            </body>
            </html>
            """, 
            recipient.getUsername(),
            eventTitle,
            adminUsername,
            reason,
            commentContent
        );

        sendEmail(recipient.getEmail(), subject, content);
    }

}
