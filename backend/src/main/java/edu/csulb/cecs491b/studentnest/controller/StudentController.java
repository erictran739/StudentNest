package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.student.StudentResponse;
import edu.csulb.cecs491b.studentnest.service.StudentService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<StudentResponse> list() {
        return studentService.list();
    }

//    @PatchMapping("/update")
//    public ResponseEntity<?> updateStudent(@RequestBody UpdateStudentRequest){
//
//        return null;
//    }

}
