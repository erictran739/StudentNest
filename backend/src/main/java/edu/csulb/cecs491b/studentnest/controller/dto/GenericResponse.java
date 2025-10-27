package edu.csulb.cecs491b.studentnest.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Super simple builder,
 * haven't found a need for it because building a raw response entity is just as fast
 * UPDATE: Might just keep it, sometimes I forget a ResponseType needs to be passed
 * through into the ResponseEntity.body() attribute
 * NOTE: If you don't want to use this, you can write
 * return new ResponseEntity.status(HttpStatus.[STATUS]).body(Map.of([MAPPINGS]));
 * I think it's much easier to use this function
 */
public record GenericResponse(
        String message
) {
    public static ResponseEntity<?> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new GenericResponse(message));
    }

    // Take a list of values and return it as a generic response
    public static ResponseEntity<?> build(HttpStatus status, List<String> list) {
        return null;
//        return ResponseEntity
//                .status(status)
//                .body(new GenericResponse(message));
    }
}
