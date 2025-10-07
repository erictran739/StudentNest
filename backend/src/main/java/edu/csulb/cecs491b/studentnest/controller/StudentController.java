package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.student.CreateStudentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.student.StudentResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.UpdateStudentRequest;
import edu.csulb.cecs491b.studentnest.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) { this.service = service; }

    @GetMapping
    public List<StudentResponse> list() { return service.list(); }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable int id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody CreateStudentRequest req) {
        StudentResponse saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/student/" + saved.userID())).body(saved);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable int id, @Valid @RequestBody UpdateStudentRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
