package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    @Id
    private int enrollmentID;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name="section_id")
    private Section section;

    private String enrollmentDate;
}

