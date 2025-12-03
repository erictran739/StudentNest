package edu.csulb.cecs491b.studentnest.controller.dto.user;

public record UserResponse(
        int userID,
        String firstName,
        String lastName,
        String email,
        String status,
        String role
) {}
