package edu.csulb.cecs491b.studentnest.controller.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateStudentRequest(
        // User attributes
        String firstName,
        String lastName,
        @Email String email,
        @Size(min = 6) String password,
        String status
        // Student specific attributes

) {}