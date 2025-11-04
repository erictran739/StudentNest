package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "department_chairs")
@DiscriminatorValue("DEPARTMENT_CHAIR")
@Getter
@Setter
@NoArgsConstructor
public class DepartmentChair extends Admin {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id",
            foreignKey = @ForeignKey(name = "fk_departmentchair_department"))
    private Department department;     // the department the chair leads

    @Column(length = 100)
    private String building;           // building where office is located

    @Column(length = 20)
    private String roomNumber;         // room number of office

    @Column(length = 100)
    private String contactEmail;       // department contact email

    @Column(length = 20)
    private String phoneNumber;        // department phone number
}
