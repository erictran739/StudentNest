package edu.csulb.cecs491b.studentnest.controller.admin;

import edu.csulb.cecs491b.studentnest.entity.SystemAdmin;
import edu.csulb.cecs491b.studentnest.entity.enums.UserStatus;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/admins")
public class AdminManagementController {

    private final UserRepository users;
    private final PasswordEncoder enc;

    public AdminManagementController(UserRepository users, PasswordEncoder enc) {
        this.users = users;
        this.enc = enc;
    }

    // Create a new admin (admin-only)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAdminRequest r) {
        if (r == null || r.email() == null || r.email().isBlank() ||
            r.password() == null || r.password().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error","email and password required"));
        }
        if (users.existsByEmail(r.email())) {
            return ResponseEntity.status(409).body(Map.of("error","Email already in use"));
        }

        var sa = new SystemAdmin();
        sa.setFirstName(r.firstName());
        sa.setLastName(r.lastName());
        sa.setEmail(r.email());
        sa.setPassword(enc.encode(r.password()));
        sa.setStatus(UserStatus.ACTIVE);

        var saved = users.save(sa);
        return ResponseEntity.status(201).body(Map.of(
            "id", saved.getUserID(),
            "email", saved.getEmail(),
            "type", "SYSTEM_ADMIN"
        ));
    }

    // Optional utilities (handy for admin panel)

    @GetMapping
    public ResponseEntity<?> listAdmins() {
        // simple filter without custom repo: fetch all and filter by user_type via projection already present in joined table
        // For now, just return all users; frontend can filter if needed.
        return ResponseEntity.ok(users.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (!users.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error","Admin not found: "+id));
        }
        users.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record CreateAdminRequest(String firstName, String lastName, String email, String password) {}
}
