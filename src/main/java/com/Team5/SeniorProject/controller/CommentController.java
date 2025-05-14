package com.Team5.SeniorProject.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Team5.SeniorProject.service.CommentService;
import com.Team5.SeniorProject.model.PostComments;


@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    //Get the comments for the post
    @GetMapping("/{eventId}")
    public List<PostComments> getCommentForPosts(@PathVariable Long id) {
        return commentService.getCommentsByEventId(id);
    }

    
}
