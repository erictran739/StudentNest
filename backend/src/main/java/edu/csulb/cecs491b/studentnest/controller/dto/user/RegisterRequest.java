package edu.csulb.cecs491b.studentnest.controller.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    // Optional — only used when we registering students
    private String major;          //  "Computer Science"
    private int enrollmentYear;    // 2025

    private String building;
    private String roomNumber;
    private String contactEmail;
    private String phoneNumber;
    private Long departmentId;  // optional for when we linking Department later
}
