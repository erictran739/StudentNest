package edu.csulb.cecs491b.studentnest.controller.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddSectionRequest(
        @Positive int course_id,
        int professor_id,
        @Positive int capacity,
        @NotEmpty String building,
        @NotEmpty String roomNumber,
        @NotEmpty String type,        //Lab, Lecture, Seminar etc
        @NotEmpty String term,        // Winter/Spring/Summer/Fall
        @NotEmpty String startDate,         // MM/DD/YY
        @NotEmpty String endDate,         // MM/DD/YY
        @NotEmpty String startTime,
        @NotEmpty String endTime
) {
}
