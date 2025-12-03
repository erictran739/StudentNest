package edu.csulb.cecs491b.studentnest.controller.dto.student;

import jakarta.validation.constraints.NotBlank;

public record EnrollSectionRequest(
        @NotBlank int student_id,
        @NotBlank int section_id,
        String enrollment_date
        ) {
}
