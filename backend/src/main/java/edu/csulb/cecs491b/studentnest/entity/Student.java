package edu.csulb.cecs491b.studentnest.entity;

import edu.csulb.cecs491b.studentnest.objects.Major;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "student")
public class Student extends User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentID;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private int enrollmentYear;

    @Column(nullable = false)
    private String enrollmentStatus;

    private float GPA;
}
