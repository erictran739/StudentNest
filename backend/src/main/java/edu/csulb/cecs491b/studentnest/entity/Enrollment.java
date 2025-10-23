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
    @EmbeddedId
    private EnrollmentID enrollmentID;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "user_id")
    private Student student;

    @ManyToOne
    @MapsId("sectionID")
    @JoinColumn(name = "section_id")
    private Section section;

    private String enrollmentDate;
    private char grade; // A-D, F, P,
}

