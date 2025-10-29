package edu.csulb.cecs491b.studentnest.controller.dto.course;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Section;

public record DeleteSectionRequest(
        int courseID
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
    public static Section fromRequest(DeleteSectionRequest request, Course course) {
        Section section = new Section();
        section.setCourse(course);
        return section;
    }
}
