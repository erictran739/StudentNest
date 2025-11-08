package edu.csulb.cecs491b.studentnest.controller.dto;

/**
 * DTO for updating an existing Department.
 */
public record UpdateDepartmentRequest(
        String name,
        String description,
        String abbreviation
) {}
