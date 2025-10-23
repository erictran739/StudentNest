package edu.csulb.cecs491b.studentnest.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public record ErrorResponse(String error, String detail, Instant timestamp) {
    public static ErrorResponse of(String error, String detail) {
        return new ErrorResponse(error, detail, Instant.now());
    }

    public static ResponseEntity<ErrorResponse> build(HttpStatus status, String errorMessage) {
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, "detail", Instant.now());

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }
}