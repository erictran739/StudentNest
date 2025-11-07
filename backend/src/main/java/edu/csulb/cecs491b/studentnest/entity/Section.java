package edu.csulb.cecs491b.studentnest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private Course course = null;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor = null;

    private int capacity = 0;
    private int enrollCount = 0;

    private String building = null;
    private String roomNumber = null;

    //Lab, Lecture, etc (enum?)
    private String type = null;

    // Winter/Spring/Summer/Fall (enum?)
    private String term = null;

    // MM/dd/yyyy
    private String startDate = null;
    private String endDate = null;

    // HH:mm
    private String startTime = null;
    private String endTime = null;
}
