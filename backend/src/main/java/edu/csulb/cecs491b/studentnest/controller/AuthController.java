package edu.csulb.cecs491b.studentnest.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.csulb.cecs491b.studentnest.controller.dto.AuthResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.LoginRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.RegisterRequest;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.entity.UserStatus;

//this is 
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserRepository users;
	private final PasswordEncoder passwordEncoder;
	
	
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (users.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse("error", "Email already exists"));
        }
        //create a student as the concrete subtype
        Student s = new Student();
        s.setFirstName(req.getFirstName());
        s.setLastName(req.getLastName());
        s.setEmail(req.getEmail());
        s.setPassword(passwordEncoder.encode(req.getPassword()));
        s.setStatus(UserStatus.ACTIVE);
        
        //this line we will add after 
        //s.setMajor(Major.UNDECLARED);
        //s.setEnrollmentYear(2025);
        users.save(s);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse("registered", req.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return users.findByEmail(req.getEmail())
                .map(u -> {
                    boolean ok = passwordEncoder.matches(req.getPassword(), u.getPassword());
                    if (!ok) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials", null));
                    }
                    // (later: return JWT or session info)
                    return ResponseEntity.ok(new AuthResponse("Login successful", req.getEmail()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Invalid credentials", null)));
    }
}
