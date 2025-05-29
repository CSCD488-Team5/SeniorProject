package com.Team5.SeniorProject.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Team5.SeniorProject.model.PostComments;
import com.Team5.SeniorProject.service.CommentService;

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

    //Create a comment for that post
    @PostMapping("/{eventid}/postComment")
    public ResponseEntity<Void> postComment (@PathVariable ("eventid") Long id, @RequestBody Map<String, String> payload, Principal principal){

        String userName = principal.getName();
        String comment = payload.get("comment");
        String timeStamp = payload.get("timeStamp");
        commentService.createCommnent(id, userName, comment, timeStamp);
        return ResponseEntity.ok().build(); 
    }

    @DeleteMapping("/{commentid}/deleteComment")
    public ResponseEntity<Void> deleteComment(@PathVariable ("commentid") Long commentId, Principal principal){

        String userName = principal.getName();
        commentService.deleteComment(userName, commentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{commentId}/admin-delete")
    public ResponseEntity<?> deleteCommentByAdmin(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> body,
            @RequestParam String username) {
        
        String reason = body.get("reason");
        if (reason == null || reason.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Deletion reason is required.");
        }

        try {
            commentService.deleteCommentByAdmin(commentId, username, reason);
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
