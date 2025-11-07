package edu.csulb.cecs491b.studentnest.controller.dto.student;

public record CourseHistoryRequest(
        int student_id,
        String term,
        int year
) {
}
