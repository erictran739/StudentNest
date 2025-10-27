package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@Getter @Setter
@NoArgsConstructor
public abstract class Admin extends User {

    @Column(length = 64)
    private String roleTitle = "System Admin";

    @Column(length = 32)
    private String permissions = "ALL";
}
