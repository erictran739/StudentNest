package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="course_id")
    private int courseID;

    @OneToMany(mappedBy = "course")
    private List<Section> sections;

    private String name;
    private String abbreviation;
    private String description;
    private Department department;

}
