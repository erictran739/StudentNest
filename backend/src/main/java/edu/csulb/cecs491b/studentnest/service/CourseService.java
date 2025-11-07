package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.course.*;
import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.entity.Department;
import edu.csulb.cecs491b.studentnest.repository.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    public ResponseEntity<?> getById(int id) {
        Course course = getCourse(id);

        return CourseResponse.build(HttpStatus.OK, course);
    }

    public ResponseEntity<?> getByName(String name) {
        Course course = getCourse(name);
        return CourseResponse.build(HttpStatus.OK, course);
    }

    public List<CourseResponse> getCoursesByDeptAbbr(String department_abbreviation) {
        Department department = departmentRepository.findByAbbreviation(department_abbreviation).orElseThrow(
                () -> new NoSuchElementException("Department with abbreviation [" + department_abbreviation + "] does not exists")
        );

        return courseRepository.findAllByDepartmentIs(department)
                .stream().map(CourseResponse::build).toList();
    }

    public List<CourseResponse> getCoursesByDeptName(String department_name) {
        Department department = departmentRepository.findByName(department_name).orElseThrow(
                () -> new NoSuchElementException("Department with name [" + department_name + "] does not exists")
        );

        return courseRepository.findAllByDepartmentIs(department)
                .stream().map(CourseResponse::build).toList();
    }


    public List<CourseResponse> getCourses(CourseRequest request) {
        List<CourseResponse> courses = new ArrayList<>();

        if (request.department_abbreviation() != null){
            courses.addAll(getCoursesByDeptAbbr(request.department_abbreviation()));
        }

        if (request.department_name() != null){
            courses.addAll(getCoursesByDeptName(request.department_name()));
        }

        if (request.course_name() != null){
            courses.add(CourseResponse.build(getCourse(request.course_name())));
        }

        if (request.course_id() > 0) {
            courses.add(CourseResponse.build(getCourse(request.course_id())));
        }

        return courses;
    }

    public ResponseEntity<?> create(CreateCourseRequest request) {
        // Create course
        Course course = new Course();
        course.setName(request.name());
        course.setDescription(request.description());
        course.setCredits(request.credits());

        // Check to see if department exists
        Department department = departmentRepository.findByAbbreviation(request.department_abbreviation()).orElseThrow(
                () -> new NoSuchElementException("Department with abbreviation [" + request.department_abbreviation() + "] does not exists")
        );

        course.setDepartment(department);

        // Save course
        courseRepository.save(course);

        return CourseResponse.build(HttpStatus.OK, course);
    }

    public ResponseEntity<?> addSection(AddSectionRequest request) {
        // Verify course exists
        Course course = getCourse(request.course_id());

        // Create section for this course and save
//        Section section = AddSectionRequest.fromRequest(request, courseOptional.get());
        Section section = new Section();
        section.setCourse(course);
        sectionRepository.save(section);

//        if (sectionRepository.findById(section.getSectionID()).isEmpty()){
//            return GenericResponse.build(HttpStatus.BAD_REQUEST, "Course could not be created: unknown reason");
//        }

        return SectionResponse.build(HttpStatus.OK, section);
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
        return sectionRepository.findAllByCourseIs(course)
                .stream().map(SectionResponse::build).toList();
    }

    public List<CourseResponse> listAll() {
        return courseRepository.findAll().stream().map(CourseResponse::build).toList();
    }

    // Helper functions
    public Course getCourse(int id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Course with course_id [" + id + "] does not exist")
        );
    }

    public Course getCourse(String name) {
        return courseRepository.findByName(name).orElseThrow(
                () -> new NoSuchElementException("Course with course_name [" + name + "] does not exist")
        );
    }

    public Section getSection(int id) {
        return sectionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Section with course_id [" + id + "] does not exist")
        );
    }
}
