package edu.csulb.cecs491b.studentnest.controller.dto.course;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Section;

public record DeleteSectionRequest(
        int sectionID
) {
    public static Section fromRequest(DeleteSectionRequest request, Course course) {
        Section section = new Section();
        section.setCourse(course);
        return section;
    }
}
