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
    private int course_id;
    private String name;
    private String description;
    private String department_abbreviation;
    private int credits;

    public static ResponseEntity<?> build(HttpStatus status, Course course) {
        CourseResponse courseResponse = new CourseResponse(
                course.getCourseID(),
                course.getName(),
                course.getDescription(),
                course.getDepartment().getAbbreviation(),
                course.getCredits()
        );

        return GenericResponse.build(status, courseResponse);
    }

    public static CourseResponse build(Course course){
       return new CourseResponse(
                course.getCourseID(),
                course.getName(),
                course.getDescription(),
                course.getDepartment().getAbbreviation(),
                course.getCredits()
        );
    }
}
