package com.Team5.SeniorProject.service;

import com.Team5.SeniorProject.repository.CommentRepository;
import com.Team5.SeniorProject.model.PostComments;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public PostComments createComment(PostComments comment){
        return commentRepository.save(comment);
    }

    public List<PostComments> getCommentsByEventId(Long eventId){
        return commentRepository.findByEventId(eventId);
        }


    
}
