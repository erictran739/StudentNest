package edu.csulb.cecs491b.studentnest.controller.dto.student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStudentRequest(
		
		@NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @Size(min = 6) String password,
        @NotBlank String status // active/inactive/locked
) {}
		
