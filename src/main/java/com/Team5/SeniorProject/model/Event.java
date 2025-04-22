package com.Team5.SeniorProject.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String subtitle;
	private String Category;
	private String description;
	private LocalDateTime time;
	private String location;
	@Lob
	private String imageUrl;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
