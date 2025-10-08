package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.AuthResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.LoginRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.RegisterRequest;
import edu.csulb.cecs491b.studentnest.entity.Professor;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.repository.ProfessorRepository;
import edu.csulb.cecs491b.studentnest.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
//this is
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterRequest req) {

        // Check if email already belongs to an account
        if (studentRepository.existsByEmail(req.getEmail()) || professorRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        // Build new student object/entity
        Student student = new Student();
        student.setFirstName(req.getFirstName());
        student.setLastName(req.getLastName());
        student.setEmail(req.getEmail());
        student.setPassword(passwordEncoder.encode(req.getPassword()));
        student.setStatus("Enrolled");

        // Save new student object/entity
        studentRepository.save(student);

        // Respond with 200 code
        return ResponseEntity.ok(new AuthResponse("registered", req.getEmail()));
    }

    @PostMapping("/register/professor")
    public ResponseEntity<?> registerProfessor(@RequestBody RegisterRequest req) {

        if (studentRepository.existsByEmail(req.getEmail()) || professorRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        Professor u = new Professor();
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        professorRepository.save(u);

        return ResponseEntity.ok(new AuthResponse("registered", req.getEmail()));
    }

    @PostMapping("/login/student")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String reqEmail = req.getEmail();
        String reqPassword = req.getPassword();

        if (!studentRepository.existsByEmail(reqEmail)) {
            return ResponseEntity.status(401).body(Map.of("error", "Email not in use"));
        }
        Student student = studentRepository.findByEmail(reqEmail);

        if (passwordEncoder.matches(reqPassword, student.getPassword())) {
            return ResponseEntity.ok(new AuthResponse("ok", req.getEmail()));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }
    }

//    @PostMapping("/login/professor")
//    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
//        String reqEmail = req.getEmail();
//        String reqPassword = req.getPassword();
//
//
//
//        return userRepository.findByEmail(req.getEmail())
//                .map(u -> {
//                    if (passwordEncoder.matches(req.getPassword(), u.getPassword())) {
//                        return ResponseEntity.ok(new AuthResponse("ok", u.getEmail()));
//                    } else {
//                        return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
//                    }
//                })
//                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "invalid credentials")));
//    }
}
