package edu.csulb.cecs491b.studentnest.controller.dto.course;

public record UpdateCourseRequest(
        String name,
        String description,
        String department,
        int credits
) {
}
