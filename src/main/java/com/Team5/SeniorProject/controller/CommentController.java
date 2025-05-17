package com.Team5.SeniorProject.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;

import com.Team5.SeniorProject.service.CommentService;
import com.Team5.SeniorProject.model.Event;
import com.Team5.SeniorProject.model.PostComments;
import com.Team5.SeniorProject.repository.EventRepository;
import com.Team5.SeniorProject.repository.UserRepository;
import com.Team5.SeniorProject.model.User;


import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentService commentService;


    //Get the comments for the post
    @GetMapping("/{eventId}")
    public List<PostComments> getCommentForPosts(@PathVariable("eventId") Long id) {
        return commentService.getCommentsByEventId(id);
    }

    @PostMapping("/{eventid}/postComment")
    public ResponseEntity<Void> postComment (@PathVariable ("eventid") Long id, @RequestBody Map<String, String> payload, Principal principal){

        String userName = principal.getName();
        String comment = payload.get("comment");

        commentService.createCommnent(id, userName, comment);
        return ResponseEntity.ok().build(); 
    }
    
}
