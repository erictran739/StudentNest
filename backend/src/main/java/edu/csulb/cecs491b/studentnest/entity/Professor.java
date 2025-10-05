package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "professor")
public class Professor extends User{

    @Column
    private String department;
}
