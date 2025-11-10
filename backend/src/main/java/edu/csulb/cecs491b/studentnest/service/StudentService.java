package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.enrollment.EnrollmentResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.CourseHistoryRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.ErrorResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.EnrollResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.StudentResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.UpdateStudentRequest;
import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.entity.EnrollmentID;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.repository.EnrollmentRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import edu.csulb.cecs491b.studentnest.repository.StudentRepository;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional

@AllArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

//    private final PasswordEncoder; // Will use when passwords must be passed and checked

    public ResponseEntity<?> get(int id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return ErrorResponse.build(HttpStatus.BAD_REQUEST, "Student not found");
        }

        return StudentResponse.build(HttpStatus.OK, optionalStudent.get());
    }

    public List<StudentResponse> list() {
        return studentRepository.findAll().stream().map(StudentResponse::build).toList();
    }

    public ResponseEntity<?> update(UpdateStudentRequest request) {
        int studentId = request.studentId();
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new NoSuchElementException("Student with id [" + studentId + "] not exist")
        );

        // NOTE: the user attributes should probably be sent to UserService.update()
        // User attributes
        if (!request.firstName().isBlank())
            student.setFirstName(request.firstName());

        if (!request.lastName().isBlank())
            student.setLastName(request.lastName());

        if (!request.email().isBlank())
            student.setEmail(request.email());

        if (!request.password().isBlank())
            student.setPassword(request.password());

//        if (!request.status().isBlank())

        // Student specific attributes
        if (!request.enrollmentStatus().isBlank())
            student.setEnrollmentStatus(request.enrollmentStatus());

        // These should be handled by the backend

//        if (!request.major().isBlank())

//        if (!request.gpa())

//        if (!request.enrollmentYear())
        return StudentResponse.build(HttpStatus.OK, student);
    }

    public ResponseEntity<?> enroll(int userId, int sectionId) {
        // Get Student
        Student student = getStudent(userId);

        // Get Section
        Section section = getSection(sectionId);

        // Check if already enrolled
        EnrollmentID enrollmentID = new EnrollmentID(userId, sectionId);
        boolean exists = enrollmentRepository.existsById(enrollmentID);
        if (exists){
            return GenericResponse.build(HttpStatus.BAD_REQUEST, "Student already enrolled in this section");
        }

        // Create enrollment and save
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setSection(section);
        enrollment.setEnrollmentID(enrollmentID);

        enrollmentRepository.save(enrollment);

        return EnrollResponse.build(HttpStatus.OK, userId, sectionId, "Student successfully added to section");
    }

    public ResponseEntity<?> drop(int studentID, int sectionID) {
        // Get Student
        getStudent(studentID);

        // Get Section
        getSection(sectionID);

        // Check if student enrolled in section
        EnrollmentID enrollmentID = new EnrollmentID(studentID, sectionID);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentID);
        if (optionalEnrollment.isEmpty()) {
            return ErrorResponse.build(HttpStatus.BAD_REQUEST, "Enrollment not found");
        }

        // Get enrollment and delete
        enrollmentRepository.delete(optionalEnrollment.get());
        return GenericResponse.build(HttpStatus.OK, "Dropped student");
    }

    public List<EnrollmentResponse> getCourseHistory(CourseHistoryRequest request){
        getStudent(request.student_id());
        return enrollmentRepository.findAllByEnrollmentID_UserID(request.student_id()).stream().map(
                (enrollment) -> EnrollmentResponse.build(enrollment, enrollment.getSection())
        ).toList();
    }

    // Helper functions
    Student getStudent(int userId){
        return studentRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Student with id [" + userId + "] not found")
        );
    }
    Section getSection(int sectionId){
        return sectionRepository.findById(sectionId).orElseThrow(
        () -> new NoSuchElementException("Section with id [" + sectionId + "] not found")
        );
    }
}
