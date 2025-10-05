package edu.csulb.cecs491b.studentnest.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    private String message;
    private String email;
    
    public AuthResponse(String message, String email) {
        this.message = message;
        this.email = email;
    }
}
