package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Data
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private int sectionID;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    int capacity;
    int enrollCount;

    String building;
    String roomNumber;

    //Lab, Lecture, etc (enum?)
    String type;

    // Winter/Spring/Summer/Fall (enum?)
    String term;

    // MM/dd/yyyy
    String startDate;
    String endDate;

    // HH:mm
    String startTime;
    String endTime;
}
