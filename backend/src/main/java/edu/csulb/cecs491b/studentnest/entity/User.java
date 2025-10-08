package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter
@NoArgsConstructor 

@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
	indexes = @Index(name = "idx_users_email", columnList = "email"))

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", length = 32)

public abstract class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usrid")		// this is help hibernate use exact name it expects
    private int userID;          // unique identifier for the user

    @Column(nullable = false)
    private String firstName;    // user's first name
    
    @Column(nullable = false)
    private String lastName;     // user's last name
    
    @Column(nullable = false, length = 255)   // helps DB enforce not null + reasonable length 
    private String email;        // user's email
    
    @Column(nullable = false)
    private String password;     // user's password (plain for now — will hash later)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length =16)    
    private UserStatus status = UserStatus.ACTIVE;       // active/inactive/locked
    
     
}
