package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UserResponse;
import edu.csulb.cecs491b.studentnest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping
    public List<UserResponse> list() { return service.list(); }

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

//    @PutMapping("/{id}")
//    public UserResponse update(@PathVariable int id, @Valid @RequestBody UpdateUserRequest req) {
//        return service.update(id, req);
//    }
    
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
