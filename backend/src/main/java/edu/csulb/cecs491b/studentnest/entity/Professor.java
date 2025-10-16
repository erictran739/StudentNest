package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professors")
@DiscriminatorValue("PROFESSOR")
@Getter @Setter
@NoArgsConstructor
public class Professor extends User {

    @Column(length = 100)
    private String department;

    @Column(length = 50)
    private String office;

    // You can later add a OneToMany<Course> relationship if needed.
}
