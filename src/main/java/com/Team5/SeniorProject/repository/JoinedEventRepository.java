package com.Team5.SeniorProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Team5.SeniorProject.model.JoinEvent;

@Repository
public interface JoinedEventRepository extends JpaRepository<JoinEvent, Long>{
    List<JoinEvent> findByEvent_Id(Long eventId);
    List<JoinEvent> findByUser_Username(String username);

}
