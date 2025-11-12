package edu.csulb.cecs491b.studentnest.controller.dto.professor;

import jakarta.validation.constraints.Positive;

public record AssignSectionRequest(
        @Positive int professor_id,
        @Positive int section_id) {
}
