package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.ErrorResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.section.EnrollResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.StudentResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.user.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UserResponse;
import edu.csulb.cecs491b.studentnest.entity.*;
import edu.csulb.cecs491b.studentnest.entity.enums.UserStatus;
import edu.csulb.cecs491b.studentnest.repository.EnrollmentRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import edu.csulb.cecs491b.studentnest.repository.StudentRepository;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

//    private final PasswordEncoder; // Will use when passwords must be passed and checked

    public UserService(UserRepository userRepository,
     SectionRepository sectionRepository,
     EnrollmentRepository enrollmentRepository,
     StudentRepository studentRepository
     ) {
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }



    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse get(int id) {
        return userRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public UserResponse create(CreateUserRequest r) {
        if (userRepository.existsByEmail(r.email()))
            throw new IllegalArgumentException("Email already in use");

        Student s = new Student();
        s.setFirstName(r.firstName());
        s.setLastName(r.lastName());
        s.setEmail(r.email());
        s.setPassword(r.password()); // TODO: BCrypt later
        s.setStatus(parseStatusOrDefault(r.status(), UserStatus.ACTIVE));
        return toResponse(userRepository.save(s));
    }

    public UserResponse partialUpdate(int id, UpdateUserRequest r) {
        User u = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        if (r.firstName() != null && !r.firstName().isBlank()) u.setFirstName(r.firstName());
        if (r.lastName()  != null && !r.lastName().isBlank())  u.setLastName(r.lastName());
        if (r.email()     != null && !r.email().isBlank())     u.setEmail(r.email());
        if (r.password()  != null && !r.password().isBlank())  u.setPassword(r.password()); // TODO: hash
        if (r.status()    != null && !r.status().isBlank())    u.setStatus(parseStatusOrDefault(r.status(), u.getStatus()));

        return toResponse(userRepository.save(u));
    }

    public void delete(int id) {
        if (!userRepository.existsById(id)) throw new IllegalArgumentException("User not found: " + id);
        userRepository.deleteById(id);
    }

    // We don't know what kind of response yet
    // The frontend can check by the status code first, then decide how to parse it?
    public ResponseEntity<?> enroll(int user_id, int section_id){
        // Get Student
        Optional<Student> optionalStudent = studentRepository.findById(user_id);
        if (optionalStudent.isEmpty()){
            return EnrollResponse.build(HttpStatus.BAD_REQUEST, "The user was not found", user_id, section_id);
        }

        // Get Section
        Optional<Section> sectionOptional = sectionRepository.findById(section_id);


        if (sectionOptional.isEmpty()){
            return EnrollResponse.build(HttpStatus.BAD_REQUEST, "The section was not found", user_id, section_id);
        }

        //TODO: Check if the student is already enrolled in the section
//        boolean exists = enrollmentRepository.existsByUserIdAndSectionId(user_id, section_id);
//        if (exists){
//            return EnrollResponse.build(HttpStatus.BAD_REQUEST, "The user is already enrolled in this section", user_id, section_id);
//        }

        // Create enrollment and save
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(optionalStudent.get());
        enrollment.setSection(sectionOptional.get());

        EnrollmentID enrollmentID = new EnrollmentID(user_id, section_id);
        enrollment.setEnrollmentID(enrollmentID);
        enrollmentRepository.save(enrollment);

        return EnrollResponse.build(HttpStatus.OK, "Student successfully added to section", user_id, section_id);
    }

    public ResponseEntity<?> drop(int studentID, int sectionID) {
        EnrollmentID enrollmentID = new EnrollmentID(studentID, sectionID);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentID);
        if (optionalEnrollment.isEmpty()){
            return ErrorResponse.build(HttpStatus.BAD_REQUEST, "Enrollment not found");
        }

        // Get enrollment and delete
        enrollmentRepository.delete(optionalEnrollment.get());
        return GenericResponse.build(HttpStatus.OK, "Dropped student");
    }

    // Helper Functions

    private UserResponse toResponse(User u) {
        //if UserResponse expects string for status, pass name()
        return new UserResponse(
                u.getUserID(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getStatus().name()
        );
    }

    private static UserStatus parseStatusOrDefault(String s, UserStatus dflt) {
        if (s == null) return dflt;
        try {
            return UserStatus.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return dflt;
        }
    }

}
