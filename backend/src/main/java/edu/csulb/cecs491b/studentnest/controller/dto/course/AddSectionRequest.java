package edu.csulb.cecs491b.studentnest.controller.dto.course;

import jakarta.validation.constraints.NotNull;

public record AddSectionRequest(
        @NotNull int course_id,
        int capacity,
        String building,
        String roomNumber,
        String type,        //Lab, Lecture, Seminar etc
        String term,        // Winter/Spring/Summer/Fall
        String startDate,         // MM/DD/YY
        String endDate,         // MM/DD/YY
        String startTime,
        String endTime

) {
}
