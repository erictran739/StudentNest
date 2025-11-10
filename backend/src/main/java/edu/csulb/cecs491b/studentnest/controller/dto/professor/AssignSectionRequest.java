package edu.csulb.cecs491b.studentnest.controller.dto.professor;

import jakarta.validation.constraints.NotBlank;

public record AssignSectionRequest(
        @NotBlank int professor_id,
        @NotBlank int section_id) {
}
