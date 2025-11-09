package edu.csulb.cecs491b.studentnest.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        @Email String email,
        @Size(min = 6) String password,
        String status
) {}