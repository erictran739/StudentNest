package edu.csulb.cecs491b.studentnest.controller.dto;

/**
 * DTO for creating a new Department.
 */
public record CreateDepartmentRequest(
        String name,
        String abbreviation,
        String description
) {}
