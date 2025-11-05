package edu.csulb.cecs491b.studentnest.controller.dto.course;

public record CourseRequest(
        // Course attributes
        int course_id,
        String course_name,
//        String course_description,

        // Department attributes
        String department_name,
        String department_abbreviation
) {
}
