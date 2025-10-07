package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.StudentResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UserResponse;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    public List<StudentResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public StudentResponse get(long id) {
        Optional<Student> optionalStudent = repo.findById(id);
        return optionalStudent.map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }

    public StudentResponse create(CreateUserRequest r) {
        if (repo.existsByEmail(r.email()))
            throw new IllegalArgumentException("Email already in use");

        Student u = new Student();
        u.setFirstName(r.firstName());
        u.setLastName(r.lastName());
        u.setEmail(r.email());
        u.setPassword(r.password()); // TODO: BCrypt later
        return toResponse(repo.save(u));
    }

    public StudentResponse update(int id, UpdateUserRequest r) {
        Student u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        u.setFirstName(r.firstName());
        u.setLastName(r.lastName());
        u.setEmail(r.email());
        u.setPassword(r.password()); // TODO: BCrypt later
        return toResponse(repo.save(u));
    }

    public void delete(long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("User not found: " + id);
        repo.deleteById(id);
    }

    private StudentResponse toResponse(Student u) {
        return new StudentResponse(
                u.getUserID(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getStatus()
        );
    }
}
