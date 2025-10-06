package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UserResponse;
import edu.csulb.cecs491b.studentnest.entity.User;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

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

        User u = new User();
        u.setFirstName(r.firstName());
        u.setLastName(r.lastName());
        u.setEmail(r.email());
        u.setPassword(r.password()); // TODO: BCrypt later
        u.setStatus(r.status());
        return toResponse(repo.save(u));
    }

    public UserResponse update(int id, UpdateUserRequest r) {
        User u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        u.setFirstName(r.firstName());
        u.setLastName(r.lastName());
        u.setEmail(r.email());
        u.setPassword(r.password()); // TODO: BCrypt later
        u.setStatus(r.status());
        return toResponse(repo.save(u));
    }

    public void delete(int id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("User not found: " + id);
        repo.deleteById(id);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getUserID(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getStatus()
        );
    }
}
