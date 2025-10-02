package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Professor")
public class Professor extends User{
    @Id
    private Long facultyID;


}
