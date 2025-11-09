package edu.csulb.cecs491b.studentnest.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Super simple builder,
 * haven't found a need for it because building a raw response entity is just as fast
 * UPDATE: Might just keep it, sometimes I forget the syntax of creating a ResponseEntity
 * from scratch.
 * NOTE: If you don't want to use this, you can write
 * return new ResponseEntity.status(HttpStatus.[STATUS]).body(Map.of([MAPPINGS]));
 * I think it's much easier to use this function
 */
@Getter
@Setter
@Data

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {
    private String message;

    public static ResponseEntity<?> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new GenericResponse(message));
    }

    public static ResponseEntity<?> build(HttpStatus status, GenericResponse response) {
        return ResponseEntity
                .status(status)
                .body(response);
    }
}