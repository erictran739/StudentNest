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
    private int section_id;

    private int course_id;
    private String course_name;


    private int professor_id;
    private String professor_name;

    private int capacity;
    private int enroll_count;
    private String start_time;
    private String end_time;
    private String building;
    private String room_number;
    private String type;        //Lab, Lecture, etc.
    private String term;        // Winter/Spring/Summer/Fall
    private String start_date;         // MM/DD/YY
    private String end_date;         // MM/DD/YY

    public static ResponseEntity<?> build(HttpStatus status, Section section) {
        int professorId = (section.getProfessor() == null) ? -1 : section.getProfessor().getUserID();
        String professorName = (section.getProfessor() == null) ?
        "TBD" :
        section.getProfessor().getFirstName() + " " + section.getProfessor().getLastName();

        SectionResponse sectionResponse = new SectionResponse(
                section.getSectionID(),

                section.getCourse().getCourseID(),
                section.getCourse().getName(),

                professorId,
                professorName,

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
                section.getSectionID(),

                section.getCourse().getCourseID(),
                section.getCourse().getName(),

                section.getProfessor().getUserID(),
                section.getProfessor().getFirstName(),

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
