package edu.csulb.cecs491b.studentnest.controller.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String description,
        @NotNull @NotBlank String department,
        @NotNull @NotBlank int credits
) {
}
