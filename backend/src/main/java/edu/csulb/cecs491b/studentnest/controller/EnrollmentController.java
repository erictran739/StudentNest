package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService service) {
        this.enrollmentService = service;
    }

    /** GET enrolled classes**/
     @GetMapping("/user/{id}")
     public ResponseEntity<?> getByUser(@PathVariable int id) {
     return enrollmentService.getEnrollmentsByUserId(id);
     }

     /** ENROLL student **/
    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@RequestParam int student_id,
                                    @RequestParam int section_id) {
        return enrollmentService.enroll(student_id, section_id);
    }

    /** DROP class **/
    @PostMapping("/drop")
    public ResponseEntity<?> drop(@RequestParam int student_id,
                                  @RequestParam int section_id) {
        return enrollmentService.drop(student_id, section_id);
    }
}