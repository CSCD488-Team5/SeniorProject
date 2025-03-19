package com.Team5.SeniorProject.model;


import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)//Referencing the User Table
    private User user;


    public Post() {}

    public Post(String title, String description, String username, User user) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public String getUsername() {return username;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

}
