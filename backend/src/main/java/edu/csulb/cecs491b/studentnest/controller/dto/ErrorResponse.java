package edu.csulb.cecs491b.studentnest.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public record ErrorResponse(String detail, Instant timestamp) {
    public static ErrorResponse of(String detail) {
        return new ErrorResponse(detail, Instant.now());
    }

    public static ResponseEntity<ErrorResponse> build(HttpStatus status, String errorMessage) {
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, Instant.now());

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }
}