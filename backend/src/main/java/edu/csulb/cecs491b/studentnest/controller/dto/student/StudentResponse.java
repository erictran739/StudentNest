package edu.csulb.cecs491b.studentnest.controller.dto.student;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Getter
@Setter

@AllArgsConstructor
public class StudentResponse extends GenericResponse {
    // User attributes
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    // Student specific attributes
    private String major;
    private String enrollmentStatus;
    private float gpa;
    private int enrollmentYea;

    public static ResponseEntity<?> build(HttpStatus status, Student student) {
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

        return GenericResponse.build(status, studentResponse);
    }

    public static StudentResponse build(Student student){
        return new StudentResponse(
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
    }
}
