package edu.csulb.cecs491b.studentnest.entity;

import edu.csulb.cecs491b.studentnest.controller.dto.StudentResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Setter
@Getter
@Entity
@Table(name = "student")
public class Student extends User {
    public Student (){ }
    public Student(String firstName, String lastName, String email, String password){
        super(firstName, lastName, email, password);
    }
//    @Column(nullable = false)
//    private String major;

    @Column(nullable = false)
    @Value("${GPA: 0.0}")
    private float GPA = 0.0f;


//    @Column(nullable = false)
//    private int enrollmentYear;

//    @Column(nullable = false)
//    private String enrollmentStatus;
}
