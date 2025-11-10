package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.user.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UserResponse;
import edu.csulb.cecs491b.studentnest.entity.*;
import edu.csulb.cecs491b.studentnest.entity.enums.UserStatus;
import edu.csulb.cecs491b.studentnest.repository.EnrollmentRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import edu.csulb.cecs491b.studentnest.repository.StudentRepository;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

//    private final PasswordEncoder; // Will use when passwords must be passed and checked

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
        var u = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        userRepository.delete(u);
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
