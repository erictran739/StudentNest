package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.user.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.LoginRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UserResponse;
import edu.csulb.cecs491b.studentnest.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserResponse> list() { return service.list(); }

    @GetMapping("/id")
    public ResponseEntity<?> getFromCredentials(@Valid @RequestBody LoginRequest request) {
        return service.get(request.getEmail(), request.getPassword());
    }

    @GetMapping("/{id}")
//    public UserResponse get(@PathVariable int id) { return service.get(id); }
    
    public ResponseEntity<?> get(@PathVariable int id) {
        try {
            return ResponseEntity.ok(service.get(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        UserResponse saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/users/" + saved.userID())).body(saved);
    }

    
    @PatchMapping("/{id}")
    public UserResponse patch(@PathVariable int id, @RequestBody UpdateUserRequest req) {
    	return service.partialUpdate(id, req);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // "User not found: X"
        }
    }
}
