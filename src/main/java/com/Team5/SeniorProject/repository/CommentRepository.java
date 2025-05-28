package com.Team5.SeniorProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Team5.SeniorProject.model.PostComments;

import java.util.List;

public interface CommentRepository extends JpaRepository<PostComments, Long>{
    List<PostComments> findByEventId(long eventId);
    List<PostComments> findByUser_Username(String username);
}
