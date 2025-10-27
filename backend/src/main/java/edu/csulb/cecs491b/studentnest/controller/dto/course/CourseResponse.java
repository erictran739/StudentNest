package edu.csulb.cecs491b.studentnest.controller.dto.course;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.enums.Department;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record CourseResponse(
        int courseID,
        String name,
        String description,
        Department department,
        int credits
) {
    public static ResponseEntity<CourseResponse> build(HttpStatus status, Course course) {
        CourseResponse courseResponse = new CourseResponse(
                course.getCourseID(),
                course.getName(),
                course.getDescription(),
                course.getDepartment(),
                course.getCredits()
        );

        return ResponseEntity
                .status(status)
                .body(courseResponse);
    }
}
