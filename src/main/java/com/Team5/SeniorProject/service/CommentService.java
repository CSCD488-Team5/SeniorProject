package com.Team5.SeniorProject.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.PostComments;
import com.Team5.SeniorProject.model.Role;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.repository.CommentRepository;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, EventRepository eventRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository; 
    }

    @Transactional
    public void createCommnent(Long id, String userName, String comment, String timestamp){
        User user = getUser(userName);
        Event event = getEvent(id);
        LocalDateTime timeStamp = parseTimeStamp(timestamp);

        PostComments postComment = new PostComments(event, user, comment, timeStamp);
        commentRepository.save(postComment);
    }

    @Transactional
    public List<PostComments> getCommentsByEventId(Long eventId){
        return commentRepository.findByEventId(eventId);
        }


    @Transactional
    public void deleteComment(String username, Long commentId){
        PostComments comment = getPostComment(commentId);
        User user = getUser(username);

        // Allow deletion if user is the author or an admin
        if (comment.getUser().equals(user) || user.getRole() == Role.ADMIN){
            commentRepository.delete(comment);
        } else {
            throw new RuntimeException("Only author or admin can delete the comment!");
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    //Helper Methods
        
    private User getUser(String userName){
        return userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    private Event getEvent(Long id){
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event does not exist!"));
    }

    private LocalDateTime parseTimeStamp(String timestamp){
        LocalDateTime timeStamp = LocalDateTime.parse(timestamp);

        return timeStamp;
    }

    private PostComments getPostComment(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("This post does not exist"));
    }
    
}
