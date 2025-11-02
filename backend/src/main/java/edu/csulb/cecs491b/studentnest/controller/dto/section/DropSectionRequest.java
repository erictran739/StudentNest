package edu.csulb.cecs491b.studentnest.controller.dto.section;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DropSectionRequest(
        @NotNull @NotBlank int student_id,
        @NotNull @NotBlank int section_id,
        String reason
) {
}
