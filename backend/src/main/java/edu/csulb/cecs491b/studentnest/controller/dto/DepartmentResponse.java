package edu.csulb.cecs491b.studentnest.controller.dto;

/**
 * DTO returned by DepartmentController responses.
 */
public record DepartmentResponse(
        Long id,
        String name,
        String description,
        Integer chairId,    // Nullable if no chair assigned
        String chairName    // "First Last" of chair if present
) {}
