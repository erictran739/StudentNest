package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.course.AddSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.course.CourseResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.course.CreateCourseRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.course.DeleteSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.entity.enums.Department;
import edu.csulb.cecs491b.studentnest.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional

@Getter
public class CourseService {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final DepartmentRepository departmentRepository;

    CourseService(CourseRepository courseRepo,
                  SectionRepository sectionRepo,
                  StudentRepository studentRepository,
                  EnrollmentRepository enrollmentRepository,
                  DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepo;
        this.sectionRepository = sectionRepo;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.departmentRepository = departmentRepository;
    }

    public ResponseEntity<?> get(int id) {
        Course course = getCourse(id);

        return CourseResponse.build(HttpStatus.OK, course);
    }

    public List<Course> getCourse(String dept_abbv) {
        // TODO: Finish 1st
        return null;
    }

    public ResponseEntity<?> create(CreateCourseRequest request) {
        // Create course
        Course course = new Course();
        course.setName(request.name());
        course.setDescription(request.description());
        course.setCredits(request.credits());

        // Check to see if department exists
        Department department =  departmentRepository.findByName(request.department()).orElseThrow(
                () -> new NoSuchElementException("Department with name" + request.name() + "does not exist")
        );

        course.setDepartment(department);

        // Save course
        return CourseResponse.build(HttpStatus.OK, course);
    }

    public ResponseEntity<?> addSection(AddSectionRequest request) {
        // Verify course exists
        Course course = getCourse(request.courseID());

        // Create section for this course and save
//        Section section = AddSectionRequest.fromRequest(request, courseOptional.get());
        Section section = new Section();
        section.setCourse(course);
        sectionRepository.save(section);

//        if (sectionRepository.findById(section.getSectionID()).isEmpty()){
//            return GenericResponse.build(HttpStatus.BAD_REQUEST, "Course could not be created: unknown reason");
//        }

        return GenericResponse.build(HttpStatus.OK, "Section successfully added to course");
    }

    public ResponseEntity<?> deleteSection(DeleteSectionRequest request) {
        // Verify section exists
        Section section = getSection(request.sectionID());

        // Check to see if students are enrolled in this course
        List<Enrollment> enrollments = enrollmentRepository.findAllBySectionIs(section);
        if (!enrollments.isEmpty()) {
            return GenericResponse.build(HttpStatus.BAD_REQUEST, "Section exists in enrollments. Drop users first");
        }

        // TODO: This needs to be a cascading delete in case enrollments contain this section
        // Delete section
        sectionRepository.delete(section);

        return GenericResponse.build(HttpStatus.OK, "Section successfully deleted from course");
    }

    public ResponseEntity<?> getSectionOfCourse(int courseId, int sectionId) {
        // Get Section
        Section section = getSection(sectionId);

        // Check if Section has a valid Course
        if (section.getCourse() == null) {
            return GenericResponse.build(HttpStatus.BAD_REQUEST, "Section does not contain a course");
        }

        if (section.getCourse().getCourseID() != courseId) {
            return GenericResponse.build(HttpStatus.BAD_REQUEST, "The given Section and Course are not related");
        }

        // Return Section
        return SectionResponse.build(HttpStatus.OK, section);
    }

    public List<SectionResponse> listSections(int courseId) {
        Course course = getCourse(courseId);
        return sectionRepository.findAllByCourseIs(course).stream().map(
                section -> new SectionResponse(
                        section.getCourse().getCourseID(),
                        section.getSectionID(),
                        section.getDepartment()
                )
        ).toList();
    }

    // Helper functions
    public Course getCourse(int id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Course with id [" + id + "] does not exist")
        );
    }

    public Section getSection(int id) {
        return sectionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Section with id [" + id + "] does not exist")
        );
    }
}
