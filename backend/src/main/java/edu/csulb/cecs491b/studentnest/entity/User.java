package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@MappedSuperclass
public abstract class User {
    // Getters and setters
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")		// this is help hibernate use exact name it expects
    private int userID;          // unique identifier for the user

    @Column(nullable = false)
    private String firstName;    // user's first name
    
    @Column(nullable = false)
    private String lastName;     // user's last name
    
    @Column(nullable = false, length = 255)   // helps DB enforce not null + reasonable length 
    private String email;        // user's email

    @Column(nullable = false)
    private String password;     // user's password (plain for now — will hash later)
    
    @Column(nullable = false)    
    private String status;       // active/inactive/locked
    
    public User() {}
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.status = status;
    }

}
