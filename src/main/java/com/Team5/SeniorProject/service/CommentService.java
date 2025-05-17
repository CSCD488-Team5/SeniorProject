package com.Team5.SeniorProject.service;

import com.Team5.SeniorProject.repository.CommentRepository;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.model.PostComments;
import com.Team5.SeniorProject.model.User;
import com.Team5.SeniorProject.model.Event;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void createCommnent(Long id, String userName, String comment){
        User user = getUser(userName);
        Event event = getEvent(id);

        PostComments postComment = new PostComments(event, user, comment);
        commentRepository.save(postComment);
        
    }

    @Transactional
    public List<PostComments> getCommentsByEventId(Long eventId){
        return commentRepository.findByEventId(eventId);
        }

        
    private User getUser(String userName){
        return userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    private Event getEvent(Long id){
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event does not exist!"));
    }
    
}