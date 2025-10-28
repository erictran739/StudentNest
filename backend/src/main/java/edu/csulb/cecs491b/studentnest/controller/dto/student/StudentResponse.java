package edu.csulb.cecs491b.studentnest.controller.dto.student;

import edu.csulb.cecs491b.studentnest.entity.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record StudentResponse(
        // User attributes
        int userID,
        String firstName,
        String lastName,
        String email,
        String status,

        // Student specific attributes
        String major,
        String enrollmentStatus,
        float gpa,
        int enrollmentYear
) {
    public static ResponseEntity<StudentResponse> build(HttpStatus status, Student student) {
        StudentResponse studentResponse = new StudentResponse(
                student.getUserID(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getStatus().toString(),
                student.getMajor().toString(),
                student.getEnrollmentStatus(),
                student.getGpa(),
                student.getEnrollmentYear()
        );

        return ResponseEntity
                .status(status)
                .body(studentResponse);
    }
}
