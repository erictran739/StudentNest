package edu.csulb.cecs491b.studentnest.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SYSTEM_ADMIN")
public class SystemAdmin extends Admin {
    // Optional: add fields (e.g., boolean superAdmin) later
}
