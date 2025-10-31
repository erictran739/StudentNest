package edu.csulb.cecs491b.studentnest.controller.dto.course;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.enums.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String description,
        @NotNull @NotBlank String department,
        @NotNull @NotBlank int credits
) {
    public static Course fromRequest(CreateCourseRequest request) {
        Department dept = Department.fromAbbreviation(request.department());
        if (dept == null){
            return null;
        }

        Course course = new Course();
        course.setName(request.name());
        course.setDescription(request.description());
        course.setDepartment(dept);
        course.setCredits(request.credits());
        return course;
    }

}
