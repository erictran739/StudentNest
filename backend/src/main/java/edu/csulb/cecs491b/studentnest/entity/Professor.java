package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "professors")
@DiscriminatorValue("PROFESSOR")
@Getter @Setter
@NoArgsConstructor
public class Professor extends User {

    @Column(length = 50)
    private String office;

    @OneToMany(mappedBy = "professor")
    private List<Section> sections;
}
