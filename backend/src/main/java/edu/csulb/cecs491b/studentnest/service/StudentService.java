package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.ErrorResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.StudentResponse;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.repository.EnrollmentRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import edu.csulb.cecs491b.studentnest.repository.StudentRepository;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

//    private final PasswordEncoder; // Will use when passwords must be passed and checked

    public StudentService(UserRepository userRepository, SectionRepository sectionRepository, EnrollmentRepository enrollmentRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }


    public ResponseEntity<?> get(int id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return ErrorResponse.build(HttpStatus.BAD_REQUEST, "Student not found");
        }

        return StudentResponse.build(HttpStatus.OK, optionalStudent.get());
    }

    public List<StudentResponse> list() {
        return studentRepository.findAll().stream()
                .map(student ->
                        new StudentResponse(
                                student.getUserID(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getEmail(),
                                student.getStatus().toString(),
                                student.getMajor().toString(),
                                student.getEnrollmentStatus(),
                                student.getGpa(),
                                student.getEnrollmentYear()
                        )
                ).toList();
    }
}
