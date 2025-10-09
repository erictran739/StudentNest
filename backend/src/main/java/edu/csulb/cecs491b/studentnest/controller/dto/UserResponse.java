package edu.csulb.cecs491b.studentnest.controller.dto;

public record UserResponse(
        int userID,
        String firstName,
        String lastName,
        String email,
        String status
) {}
