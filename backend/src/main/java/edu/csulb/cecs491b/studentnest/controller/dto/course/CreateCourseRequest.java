package edu.csulb.cecs491b.studentnest.controller.dto.course;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Department;

public record CreateCourseRequest(
        String name,
        String description,
        String department,
        int credits
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
