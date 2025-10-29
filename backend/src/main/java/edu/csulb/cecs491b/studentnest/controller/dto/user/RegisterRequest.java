package edu.csulb.cecs491b.studentnest.controller.dto.user;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
 // Optional — only used when we registering students
    private String major;          //  "Computer Science"
    private int enrollmentYear;    // 2025
}
