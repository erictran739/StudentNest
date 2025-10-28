package edu.csulb.cecs491b.studentnest.entity;

import edu.csulb.cecs491b.studentnest.entity.enums.Major;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "students")
@DiscriminatorValue("STUDENT")
@Getter @Setter
@NoArgsConstructor
public class Student extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private Major major = Major.UNDECLARED;

    @Column(nullable = false, length = 32)
    private String enrollmentStatus = "active"; // can be enum later

//     We'll add List<Enrollment> when you implement Enrollment entity (#7).
     @OneToMany(mappedBy = "student")
     private List<Enrollment> enrollments;

    @Column
    private float gpa = 0.0f;

    @Column
    private int enrollmentYear; // e.g., 2025

}
