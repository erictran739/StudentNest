package edu.csulb.cecs491b.studentnest.controller.dto;

public class AuthResponse {
    private String message;
    private String email;

    public AuthResponse(String message, String email) {
        this.message = message;
        this.email = email;
    }

    public String getMessage() { return message; }
    public String getEmail() { return email; }
}
