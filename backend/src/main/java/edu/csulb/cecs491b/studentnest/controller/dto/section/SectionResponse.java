package edu.csulb.cecs491b.studentnest.controller.dto.section;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.entity.Section;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter

@AllArgsConstructor
public class SectionResponse extends GenericResponse {
    private int courseID;
    private int sectionID;

//    int professor_id;
    int capacity;
    int enroll_count;
    String start_time;
    String end_time;
    String building;
    String room_number;
    String type;        //Lab, Lecture, etc.
    String term;        // Winter/Spring/Summer/Fall
    String start_date;         // MM/DD/YY
    String end_date;         // MM/DD/YY

    public static ResponseEntity<?> build(HttpStatus status, Section section) {
        SectionResponse sectionResponse = new SectionResponse(
                section.getCourse().getCourseID(),
                section.getSectionID(),
//                section.getProfessor().getUserID(),
                section.getCapacity(),
                section.getEnrollCount(),
                section.getStartTime(),
                section.getEndTime(),
                section.getBuilding(),
                section.getRoomNumber(),
                section.getType(),
                section.getTerm(),
                section.getStartDate(),
                section.getEndDate()
        );

        return GenericResponse.build(status, sectionResponse);
    }

    public static SectionResponse build(Section section) {
        return new SectionResponse(
                section.getCourse().getCourseID(),
                section.getSectionID(),
//                section.getProfessor().getUserID(),
                section.getCapacity(),
                section.getEnrollCount(),
                section.getStartTime(),
                section.getEndTime(),
                section.getBuilding(),
                section.getRoomNumber(),
                section.getType(),
                section.getTerm(),
                section.getStartDate(),
                section.getEndDate()
        );
    }
}
