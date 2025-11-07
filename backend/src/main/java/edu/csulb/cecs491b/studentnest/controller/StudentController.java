package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.enrollment.EnrollmentResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.section.DropSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.EnrollSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.student.CourseHistoryRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.student.StudentResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.UpdateStudentRequest;
import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.service.StudentService;
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

    @GetMapping("/history")
    public List<EnrollmentResponse> getCourseHistory(@RequestBody CourseHistoryRequest request){
        return studentService.getCourseHistory(request);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateStudent(@RequestBody UpdateStudentRequest request) {
        return studentService.update(request);
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@RequestBody EnrollSectionRequest req){
        return studentService.enroll(req.student_id(), req.section_id());
    }

    @PostMapping("/drop")
    public ResponseEntity<?> drop(@RequestBody DropSectionRequest req){
        return studentService.drop(req.student_id(), req.section_id());
    }
}
