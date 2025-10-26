package edu.csulb.cecs491b.studentnest.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import edu.csulb.cecs491b.studentnest.entity.DepartmentChair;

import edu.csulb.cecs491b.studentnest.controller.dto.AuthResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.LoginRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.RegisterRequest;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import edu.csulb.cecs491b.studentnest.entity.Admin;
import edu.csulb.cecs491b.studentnest.entity.Professor;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.entity.User;
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
        
        
        User newUser; // declare before switch

        String role = req.getRole() == null ? "" : req.getRole().toLowerCase();
        switch (role) {
            case "teacher":
            case "professor":
                newUser = new Professor();
                break;
            case "admin":
                newUser = new Admin();
                break;
                
            case "chair":
            case "departmentchair":
                DepartmentChair chair = new DepartmentChair();
                chair.setBuilding(req.getBuilding());
                chair.setRoomNumber(req.getRoomNumber());
                chair.setContactEmail(req.getContactEmail());
                chair.setPhoneNumber(req.getPhoneNumber());
                // optional: assign department when we add a Department entity
                newUser = chair;
                break;
                
            default:
            	 // 👇 This block handles STUDENT registration
                Student s = new Student();
                if (req.getMajor() != null) {
                    try {
                        s.setMajor(edu.csulb.cecs491b.studentnest.entity.Major.valueOf(
                                req.getMajor().toUpperCase()
                        ));
                    } catch (IllegalArgumentException e) {
                        s.setMajor(edu.csulb.cecs491b.studentnest.entity.Major.UNDECLARED);
                    }
                } else {
                    s.setMajor(edu.csulb.cecs491b.studentnest.entity.Major.UNDECLARED);
                }

                // set enrollment year if provided, otherwise default
                if (req.getEnrollmentYear() > 0) {
                    s.setEnrollmentYear(req.getEnrollmentYear());
                } else {
                    s.setEnrollmentYear(2025);
                }

                newUser = s;
                break;
        }
        

    // Common fields for all user types
    newUser.setFirstName(req.getFirstName());
    newUser.setLastName(req.getLastName());
    newUser.setEmail(req.getEmail());
    newUser.setPassword(passwordEncoder.encode(req.getPassword()));
    newUser.setStatus(UserStatus.ACTIVE);

        users.save(newUser);
        
        
        //create a student as the concrete subtype
//        Student s = new Student();
//        s.setFirstName(req.getFirstName());
//        s.setLastName(req.getLastName());
//        s.setEmail(req.getEmail());
//        s.setPassword(passwordEncoder.encode(req.getPassword()));
//        s.setStatus(UserStatus.ACTIVE);
//        
        //this line we will add after 
        //s.setMajor(Major.UNDECLARED);
        //s.setEnrollmentYear(2025);
//        users.save(s);
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
    
    @GetMapping("/tryagain") 
    public Map<String, String> tryagain() {
    	return Map.of("status", "ok");
    }
}
