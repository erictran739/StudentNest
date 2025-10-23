package edu.csulb.cecs491b.studentnest.controller.dto.section;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Department;
import edu.csulb.cecs491b.studentnest.entity.Section;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record SectionResponse(
        int courseID,
        int sectionID,
        Department department
        // int professorID
//        int capacity,
//        int enrollCount,
//        String startTime,
//        String endTime,
//        String building,
//        String roomNumber,
//        String type,        //Lab, Lecture, etc
//        String term,        // Winter/Spring/Summer/Fall
//        String date         // MM/DD/YY
) {
    public static ResponseEntity<SectionResponse> build(HttpStatus status, Section section, Course course) {
        SectionResponse sectionResponse = new SectionResponse(
                course.getCourseID(),
                section.getSectionID(),
                section.getDepartment()
                );

        return ResponseEntity
                .status(status)
                .body(sectionResponse);
    }
}
