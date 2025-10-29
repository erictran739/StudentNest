package edu.csulb.cecs491b.studentnest.controller.dto.section;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record EnrollResponse(
        String message,
        int userID,
        int sectionID
) {
    /**
     This is a "helper function?" It builds a response with a status code, so I don't have to write:
     ResponseEntity.status(HttpStatus.<SOME_CODE>).body(arg1, arg2, arg3);
     */
    public static ResponseEntity<EnrollResponse> build(HttpStatus status, String message, int userID, int sectionID) {
        EnrollResponse enrollResponse = new EnrollResponse(message, userID, sectionID);

        return ResponseEntity
                .status(status)
                .body(enrollResponse);
    }

}
