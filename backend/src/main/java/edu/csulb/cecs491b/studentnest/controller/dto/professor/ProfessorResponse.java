package edu.csulb.cecs491b.studentnest.controller.dto.professor;

import edu.csulb.cecs491b.studentnest.controller.ProfessorController;
import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.entity.Professor;
import edu.csulb.cecs491b.studentnest.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter

@AllArgsConstructor
public class ProfessorResponse extends GenericResponse {
    // User attributes
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    // Professor specific attributes
    private String office;

    public static ResponseEntity<?> build(HttpStatus status, Professor professor) {
        ProfessorResponse studentResponse = new ProfessorResponse(
                professor.getUserID(),
                professor.getFirstName(),
                professor.getLastName(),
                professor.getEmail(),
                professor.getStatus().toString(),
                professor.getOffice()
        );

        return GenericResponse.build(status, studentResponse);
    }

    public static ProfessorResponse build(Professor professor){
        return new ProfessorResponse(
                professor.getUserID(),
                professor.getFirstName(),
                professor.getLastName(),
                professor.getEmail(),
                professor.getStatus().toString(),
                professor.getOffice()
        );
    }
}
