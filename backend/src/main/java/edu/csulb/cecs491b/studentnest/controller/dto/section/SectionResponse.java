package edu.csulb.cecs491b.studentnest.controller.dto.section;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.enums.Department;
import edu.csulb.cecs491b.studentnest.entity.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter

@AllArgsConstructor
public class SectionResponse extends GenericResponse {
    private int courseID;
    private int sectionID;
    private Department department;

    // int professorID
    // int capacity
    // int enrollCount
    // String startTime
    // String endTime
    // String building
    // String roomNumber
    // String type        //Lab, Lecture, etc.
    // String term        // Winter/Spring/Summer/Fall
    // String date         // MM/DD/YY

    public static ResponseEntity<?> build(HttpStatus status, Section section) {
        SectionResponse sectionResponse = new SectionResponse(
                section.getCourse().getCourseID(),
                section.getSectionID(),
                section.getDepartment()
        );

        return GenericResponse.build(status, sectionResponse);
    }
}
