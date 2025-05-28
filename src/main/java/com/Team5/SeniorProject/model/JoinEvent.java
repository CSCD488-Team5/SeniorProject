package com.Team5.SeniorProject.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;




@Entity
@Table(name = "registrationsJ", uniqueConstraints = @UniqueConstraint(columnNames = {"event_id","user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "google_calendar_event_id")
    private String googleCalendarEventId;

    private LocalDateTime joined = LocalDateTime.now();   
}