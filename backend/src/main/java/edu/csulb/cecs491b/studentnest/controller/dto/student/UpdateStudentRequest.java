package edu.csulb.cecs491b.studentnest.controller.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateStudentRequest(
        // Student to be updated
        int studentId,

        // User attributes
        String firstName,
        String lastName,
        @Email String email,
        @Size(min = 8) String password,
        String status,

        // Student specific attributes
        String major,
        String enrollmentStatus,
        float gpa,
        int enrollmentYear
) {}