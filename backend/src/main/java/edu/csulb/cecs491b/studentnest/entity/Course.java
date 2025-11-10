package edu.csulb.cecs491b.studentnest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Table(name = "courses")
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
    @JoinColumn(name = "department_id",
    foreignKey = @ForeignKey(name = "fk_course_department"))
    private Department department;

    @Column(nullable = false, length = 120)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    
    private int credits;

    // TODO: Prerequisites
}
