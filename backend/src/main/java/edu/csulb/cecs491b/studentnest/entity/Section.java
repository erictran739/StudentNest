package edu.csulb.cecs491b.studentnest.entity;

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

    private Department department;
}
