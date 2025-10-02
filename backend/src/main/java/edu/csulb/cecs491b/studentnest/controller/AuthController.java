package edu.csulb.cecs491b.studentnest.controller;

import java.util.Map;

import edu.csulb.cecs491b.studentnest.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.csulb.cecs491b.studentnest.controller.dto.LoginRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.RegisterRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.AuthResponse;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        Student u = new Student();
        u.setEmail(req.getEmail());
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(u);
        return ResponseEntity.ok(new AuthResponse("registered", req.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userRepository.findByEmail(req.getEmail())
            .map(u -> {
                if (passwordEncoder.matches(req.getPassword(), u.getPassword())) {
                    return ResponseEntity.ok(new AuthResponse("ok", u.getEmail()));
                } else {
                    return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
                }
            })
            .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "invalid credentials")));
    }
}
