package edu.csulb.cecs491b.studentnest.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import edu.csulb.cecs491b.studentnest.controller.dto.GenericResponse;
import edu.csulb.cecs491b.studentnest.entity.*;
import edu.csulb.cecs491b.studentnest.entity.enums.Major;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.csulb.cecs491b.studentnest.controller.dto.AuthResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.user.LoginRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.RegisterRequest;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import edu.csulb.cecs491b.studentnest.entity.enums.UserStatus;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (users.findByEmail(req.getEmail()).isPresent()) {
            return GenericResponse.build(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        // Block raw Admin signup (Admin is a base type, not a public signup)
        if (req.getRole().equalsIgnoreCase("admin")) {
            return GenericResponse.build(HttpStatus.BAD_REQUEST, "Admin can only be created by another admin");
        }

        //this is pick the concrete subtype
        User newUser;
        switch (req.getRole().toLowerCase()) {
            case "professor":
                newUser = new Professor();
                break;
            case "chair":
            case "departmentchair":
                DepartmentChair chair = new DepartmentChair();
                chair.setBuilding(req.getBuilding());
                chair.setRoomNumber(req.getRoomNumber());
                chair.setContactEmail(req.getContactEmail());
                chair.setPhoneNumber(req.getPhoneNumber());
                newUser = chair;
                break;
            case "student":
                Student student = new Student();
                Major major = Major.fromString(req.getMajor());
                major = (major == null) ? Major.UNDECLARED : major;

                student.setMajor(major);
                student.setEnrollmentStatus("active");
                student.setGpa(0.0f);
                student.setEnrollmentYear(2025);

                newUser = student;
                break;
            default:
                return GenericResponse.build(HttpStatus.BAD_REQUEST, "Role could not be determined");
        }

        // Common fields for all user types
        newUser.setFirstName(req.getFirstName());
        newUser.setLastName(req.getLastName());
        newUser.setEmail(req.getEmail());
        newUser.setPassword(passwordEncoder.encode(req.getPassword()));
        newUser.setStatus(UserStatus.ACTIVE);

        users.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "registered", "id",
                newUser.getUserID(),     // 👈 return course_id
                "email", req.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = users.findByEmail(req.getEmail()).orElseThrow(
                () -> new NoSuchElementException("Invalid credentials")
        );

        // ugly but it works for now
        String role = "undefined";
        if (user instanceof Student){
            role = "student";
        }

        if (user instanceof Professor){
            role = "professor";
        }

        if (user instanceof DepartmentChair){
            role = "department chair";
        }

//        if (role.equals("undefined")){
//            throw new NoSuchElementException("Invalid role");
//        }

        boolean ok = passwordEncoder.matches(req.getPassword(), user.getPassword());
        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials", null));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Login Successful",
                "user_id", user.getUserID(),
                "email", user.getEmail(),
                "role", role
        ));

    }

    @GetMapping("/tryagain")
    public Map<String, String> tryagain() {
        return Map.of("status", "ok");
    }
}
