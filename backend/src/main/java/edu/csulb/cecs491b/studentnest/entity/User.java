package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usrid")		// this is help hibernate use exact name it expects
    private int userID;          // unique identifier for the user

    @Column(nullable = false)
    private String firstName;    // user's first name
    
    @Column(nullable = false)
    private String lastName;     // user's last name
    
    @Column(nullable = false, length = 255)   // helps DB enforce not null + reasonable length 
    private String email;        // user's email
    //this is 
    @Column(nullable = false)
    private String password;     // user's password (plain for now — will hash later)
    
    @Column(nullable = false)    
    private String status;       // active/inactive/locked
    
    public User() {}
    
    
    public User(String firstName, String lastName, String email, String password, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.status = status;
    }
    

    // Getters and setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
