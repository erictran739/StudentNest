package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.course.AddSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.course.CreateCourseRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.course.DeleteSectionRequest;
import edu.csulb.cecs491b.studentnest.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService service) {
        this.courseService = service;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateCourseRequest request) {
        return courseService.create(request);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        return courseService.get(id);
    }

    @PostMapping("/add/section")
    public ResponseEntity<?> addSection(@Valid @RequestBody AddSectionRequest request) {
        return courseService.addSection(request);
    }

    @PostMapping("/delete/section")
    public ResponseEntity<?> removeSection(@Valid @RequestBody DeleteSectionRequest request) {
        return courseService.deleteSection(request);
    }

    @GetMapping("/{course_id}/section/{section_id}")
    public ResponseEntity<?> getSection(@PathVariable int course_id, @PathVariable int section_id) {
        return courseService.getSectionOfCourse(course_id, section_id);
    }
}