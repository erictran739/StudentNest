package edu.csulb.cecs491b.studentnest.controller.dto.professor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfessorRequest(
        // Student to be updated
        int professor_id,

        // User attributes
        String first_name,
        String last_name,
        String email,
//        @Size(min = 8) String password,
//        String status,

        // Student specific attributes
        String office
) {}