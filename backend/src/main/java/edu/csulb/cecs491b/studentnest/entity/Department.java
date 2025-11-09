package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
public class Department {

	@OneToOne(mappedBy = "department", fetch = FetchType.LAZY)
	private DepartmentChair chair;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String abbreviation;

    @Column(length = 150)
    private String description;

    // Professors in this department
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Professor> professors = new ArrayList<>();

    // Courses offered by this department
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
}
