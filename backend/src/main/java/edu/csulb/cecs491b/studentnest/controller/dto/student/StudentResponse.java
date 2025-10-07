package edu.csulb.cecs491b.studentnest.controller.dto.student;

public record StudentResponse(
        int userID,
        String firstName,
        String lastName,
        String email,
        String status
) {}
