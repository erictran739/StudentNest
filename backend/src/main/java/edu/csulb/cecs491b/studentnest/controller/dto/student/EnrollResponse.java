package edu.csulb.cecs491b.studentnest.controller.dto.student;


import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter

@AllArgsConstructor
public class EnrollResponse extends GenericResponse {
    private int user_id;
    private int section_id;

    public static ResponseEntity<?> build(HttpStatus status, int userID, int sectionID) {
        EnrollResponse enrollResponse = new EnrollResponse(
                userID,
                sectionID
        );
        return GenericResponse.build(status, enrollResponse);
    }

    public static ResponseEntity<?> build(HttpStatus status, int userID, int sectionID, String message) {
        EnrollResponse enrollResponse = new EnrollResponse(
                userID,
                sectionID
        );
        enrollResponse.setMessage(message);
        return GenericResponse.build(status, enrollResponse);
    }

    public static ResponseEntity<?> build(HttpStatus status, EnrollResponse response) {
        return GenericResponse.build(status, response);
    }
}
