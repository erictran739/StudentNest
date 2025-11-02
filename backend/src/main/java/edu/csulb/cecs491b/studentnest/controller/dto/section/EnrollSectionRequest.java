package edu.csulb.cecs491b.studentnest.controller.dto.section;

import jakarta.validation.constraints.NotBlank;

public record EnrollSectionRequest(
        @NotBlank int student_id,
        @NotBlank int section_id) {
}
