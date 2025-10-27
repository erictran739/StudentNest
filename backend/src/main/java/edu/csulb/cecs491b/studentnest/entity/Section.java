package edu.csulb.cecs491b.studentnest.entity;

import edu.csulb.cecs491b.studentnest.entity.enums.Department;
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
    @Column(name="section_id")
    private int sectionID;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name="professor_id")
    private Professor professor_id;

    private Department department;
//        int capacity,
//        int enrollCount,
//        String startTime,
//        String endTime,
//        String building,
//        String roomNumber,
//        String type,        //Lab, Lecture, etc
//        String term,        // Winter/Spring/Summer/Fall
//        String date         // MM/DD/YY
}
