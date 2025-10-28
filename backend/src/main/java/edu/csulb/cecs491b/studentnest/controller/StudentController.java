package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.DropRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.EnrollRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.EnrollResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.user.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UserResponse;
import edu.csulb.cecs491b.studentnest.service.StudentService;
import edu.csulb.cecs491b.studentnest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService service) {
        this.studentService = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable int id) {
        return studentService.get(id);
    }

    @GetMapping
    public List<UserResponse> list() {
//        return studentService.list();
        return null;
    }


}
