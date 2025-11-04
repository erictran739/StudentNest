package edu.csulb.cecs491b.studentnest.controller.dto.course;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter

@AllArgsConstructor
public class CourseResponse extends GenericResponse {
    private int courseID;
    private String name;
    private String description;
    private Department department;
    private int credits;

    public static ResponseEntity<?> build(HttpStatus status, Course course) {
        CourseResponse courseResponse = new CourseResponse(
                course.getCourseID(),
                course.getName(),
                course.getDescription(),
                course.getDepartment(),
                course.getCredits()
        );

        return GenericResponse.build(status, courseResponse);
    }
}
