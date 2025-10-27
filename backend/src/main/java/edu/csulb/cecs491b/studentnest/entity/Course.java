package edu.csulb.cecs491b.studentnest.entity;

import edu.csulb.cecs491b.studentnest.entity.enums.Department;
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

    @ManyToOne
    @JoinColumn(name="professor_id")
    private Professor professor_id;

    private String name;
    private String description;
    private Department department;
    private int credits;

    // TODO: Prerequisites
}
