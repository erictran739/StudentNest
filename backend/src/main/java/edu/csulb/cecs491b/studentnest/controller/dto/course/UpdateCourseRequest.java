package edu.csulb.cecs491b.studentnest.controller.dto.course;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.enums.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCourseRequest(
        String name,
        String description,
        String department,
        int credits
) {
}
