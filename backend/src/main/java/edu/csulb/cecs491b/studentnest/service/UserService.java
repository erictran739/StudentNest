package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.*;
import edu.csulb.cecs491b.studentnest.controller.dto.section.EnrollResponse;
import edu.csulb.cecs491b.studentnest.entity.*;
import edu.csulb.cecs491b.studentnest.repository.EnrollmentRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
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

//    private final PasswordEncoder; // Will use when passwords must be passed and checked

    public UserService(UserRepository userRepository, SectionRepository sectionRepository, EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.enrollmentRepository = enrollmentRepository;
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
    //when you are update this will be update entire thing
//    public UserResponse update(int id, UpdateUserRequest r) {
//        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
//        //setters are on the base class works for any subtype
//        u.setFirstName(r.firstName());
//        u.setLastName(r.lastName());
//        u.setEmail(r.email());
//        u.setPassword(r.password()); // TODO: BCrypt later
//        u.setStatus(parseStatusOrDefault(r.status(), u.getStatus()));
//        return toResponse(repo.save(u));
//    }

    //on here when you update user data this call be update whatever you want to change only that thing update other things are stay same
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
    public ResponseEntity<EnrollResponse> enroll(int user_id, int section_id){
        // Get Student
        Optional<User> userOptional = userRepository.findById(user_id);
        if (userOptional.isEmpty()){ // Check if user exists
            return EnrollResponse.build(HttpStatus.NOT_FOUND, "The user was not found", user_id, section_id);
        }

        User user = userOptional.get();
        if (!(user instanceof Student student)){ // Check if user is a student
            return EnrollResponse.build(HttpStatus.BAD_REQUEST, "The user is not a student", user_id, section_id);
        }

        // Get Section
        Optional<Section> sectionOptional = sectionRepository.findById(section_id);
        if (sectionOptional.isEmpty()){
            return EnrollResponse.build(HttpStatus.NOT_FOUND, "The section was not found", user_id, section_id);
        }
        Section section = sectionOptional.get();

        //TODO: Check if the student is already enrolled in the section
//        boolean exists = enrollmentRepository.existsByUserIdAndSectionId(user_id, section_id);
//        if (exists){
//            return EnrollResponse.build(HttpStatus.BAD_REQUEST, "The user is already enrolled in this section", user_id, section_id);
//        }

        // Create enrollment and save
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setSection(section);
        enrollmentRepository.save(enrollment);

        return EnrollResponse.build(HttpStatus.OK, "Student successfully added to section", user_id, section_id);

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
