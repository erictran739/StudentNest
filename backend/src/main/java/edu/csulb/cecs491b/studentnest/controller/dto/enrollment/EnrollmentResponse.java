package edu.csulb.cecs491b.studentnest.controller.dto.enrollment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.entity.Department;
import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.entity.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EnrollmentResponse extends GenericResponse {
    private int section_id;
    private String building;
    private String room_number;
    private String type;
    private String term;
    private String start_date;
    private String end_date;
    private String start_time;
    private String end_time;

    private int course_id;
    private String course_name;
    private String department_name;
    private String department_abbreviation;

    private char grade;

    public static EnrollmentResponse build(Enrollment enrollment) {
        return null;
    }

    public static EnrollmentResponse build(Enrollment enrollment, Section section) {
        return new EnrollmentResponse(
                section.getSectionID(),
                section.getBuilding(),
                section.getRoomNumber(),
                section.getType(),
                section.getTerm(),
                section.getStartDate(),
                section.getEndDate(),
                section.getStartTime(),
                section.getEndTime(),

                section.getCourse().getCourseID(),
                section.getCourse().getName(),
                section.getCourse().getDepartment().getName(),
                section.getCourse().getDepartment().getAbbreviation(),

                enrollment.getGrade());
    }
}
