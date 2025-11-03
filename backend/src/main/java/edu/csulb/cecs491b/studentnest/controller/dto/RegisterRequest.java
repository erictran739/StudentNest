package edu.csulb.cecs491b.studentnest.controller.dto;
import lombok.Data;

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
    
    private String building;
    private String roomNumber;
    private String contactEmail;
    private String phoneNumber;
    private Long departmentId;  // optional for when we linking Department later

    
    

    public String getEmail()    { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void   setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void   setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }
    public void   setPassword(String password) { this.password = password; }
    
    public String getRole()    { return role; }
    public void   setRole(String role) { this.role = role; }
    

}
