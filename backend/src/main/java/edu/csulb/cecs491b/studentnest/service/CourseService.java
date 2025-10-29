package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.course.AddSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.course.CourseResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.ErrorResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.course.CreateCourseRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.repository.CourseRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional

@Getter
public class CourseService {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    CourseService (CourseRepository courseRepo, SectionRepository sectionRepo){
        this.courseRepository = courseRepo;
        this.sectionRepository = sectionRepo;
    }

    public ResponseEntity<?> get(int id){
        Course course = getCourse(id);
        if (course == null){
            String message = String.format("Course %d does not exist", id);
            return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }

        return CourseResponse.build(HttpStatus.OK, course);
    }

    public ResponseEntity<?> create(CreateCourseRequest request){
        // Create course
        Course course = CreateCourseRequest.fromRequest(request);
        if (course == null){
            return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, "Course could not be created: unknown reason");
        }

        courseRepository.save(course);

        // Verify course created
        if (courseRepository.findById(course.getCourseID()).isEmpty()){
            return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, "Course could not be created: unknown reason");
        }

        return CourseResponse.build(HttpStatus.OK, course);
    }

    public ResponseEntity<?> addSection(AddSectionRequest request){
        // Verify course exists
        Optional<Course> courseOptional = courseRepository.findById(request.courseID());
        if (courseOptional.isEmpty()){
            return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, "Course does not exist");
        }

        // Create section for this course and save
//        Section section = AddSectionRequest.fromRequest(request, courseOptional.get());
        Section section = new Section();
        section.setCourse(courseOptional.get());
        sectionRepository.save(section);

//        if (sectionRepository.findById(section.getSectionID()).isEmpty()){
//            return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, "Course could not be created: unknown reason");
//        }

        return GenericResponse.build(HttpStatus.OK, "Section successfully added to course");
//        return ResponseEntity.status(HttpStatus.OK).body("Section successfully added to course");
    }

    public ResponseEntity<?> getSectionOfCourse(int course_id, int section_id) {
        // Get Course
        Course course = getCourse(course_id);
        if (course == null){
            return ErrorResponse.build(HttpStatus.BAD_REQUEST, "Course does not exist");
        }

        // Get Section
        Section section = getSection(section_id);
        if (section == null){
            return ErrorResponse.build(HttpStatus.BAD_REQUEST, "Section does not exist");
        }

        // Check if Section has a valid Course
        if (section.getCourse() == null){
            return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, "Section does not contain a valid course");
        }

        if (section.getCourse().getCourseID() != course_id){
            return ErrorResponse.build(HttpStatus.BAD_REQUEST, "The given Section and Course are not related");
        }

        // Return Section
        return SectionResponse.build(HttpStatus.OK, section, course);
    }

    // Helper functions
    public Course getCourse(int id){
        Optional<Course> courseOptional = courseRepository.findById(id);
        return courseOptional.orElse(null);
    }

    public Section getSection(int id){
        Optional<Section> sectionOptional = sectionRepository.findById(id);
        return sectionOptional.orElse(null);

    }

}
