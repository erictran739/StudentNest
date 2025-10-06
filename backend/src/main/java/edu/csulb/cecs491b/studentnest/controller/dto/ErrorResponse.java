package edu.csulb.cecs491b.studentnest.controller.dto;

import java.time.Instant;

public record ErrorResponse(String error, String detail, Instant timestamp) {
    public static ErrorResponse of(String error, String detail) {
        return new ErrorResponse(error, detail, Instant.now());
    }
}