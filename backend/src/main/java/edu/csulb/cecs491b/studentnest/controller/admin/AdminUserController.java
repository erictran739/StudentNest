package edu.csulb.cecs491b.studentnest.controller.admin;

import edu.csulb.cecs491b.studentnest.controller.dto.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UserResponse;
import edu.csulb.cecs491b.studentnest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService service;

    public AdminUserController(UserService service) {
        this.service = service;
    }

    // GET /api/admin/users  -> list all users
    @GetMapping
    public List<UserResponse> list() {
        return service.list();
    }

    // GET /api/admin/users/{id} -> get one
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        try {
            return ResponseEntity.ok(service.get(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // PATCH /api/admin/users/{id} -> partial update
    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable int id, @RequestBody UpdateUserRequest req) {
        try {
            return ResponseEntity.ok(service.partialUpdate(id, req));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // DELETE /api/admin/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
