package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UserResponse;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.entity.User;
import edu.csulb.cecs491b.studentnest.entity.UserStatus;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

//    public UserService(UserRepository repo) {
//        this.repo = repo;
//    }

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse get(int id) {
        return repo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public UserResponse create(CreateUserRequest r) {
        if (repo.existsByEmail(r.email()))
            throw new IllegalArgumentException("Email already in use");

        Student s = new Student();
        s.setFirstName(r.firstName());
        s.setLastName(r.lastName());
        s.setEmail(r.email());
        s.setPassword(r.password()); // TODO: BCrypt later
        s.setStatus(parseStatusOrDefault(r.status(), UserStatus.ACTIVE));
        return toResponse(repo.save(s));
    }

    public UserResponse update(int id, UpdateUserRequest r) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

        // Check if found user has the same email in request
//        if (!u.getEmail().equals(r.email())) {
//            throw new IllegalArgumentException("Incorrect email");
//        }
//
//        boolean valid_pass = passwordEncoder.matches(r.password(), u.getPassword());
//        if (!valid_pass) {
//            throw new IllegalArgumentException("Incorrect password");
//        }
//
        //setters are on the base class works for any subtype
        u.setFirstName(r.firstName());
        u.setLastName(r.lastName());
        u.setEmail(r.email());
        u.setPassword(r.password()); // TODO: BCrypt later
        u.setStatus(parseStatusOrDefault(r.status(), u.getStatus()));
        return toResponse(repo.save(u));
    }

    public void delete(int id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("User not found: " + id);
        repo.deleteById(id);
    }

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
