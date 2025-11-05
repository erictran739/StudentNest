package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.course.*;
import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get/dept_abbr/{department_abbreviation}")
    public List<CourseResponse> getByDeptAbbr(@PathVariable String department_abbreviation) {
        return courseService.getCoursesByDeptAbbr(department_abbreviation);
    }

    @GetMapping("/get/dept_name/{name}")
    public List<CourseResponse> getByName(@PathVariable String name) {
        return courseService.getCoursesByDeptName(name);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return courseService.getById(id);
    }

    /**
     * This is supposed to serve as a catch-all. The request can contain a mix of several
     * different attributes and return back the intersection between all the attributes
     * TODO: Low Priority
     */
    @GetMapping("/get")
    public List<CourseResponse> get(@RequestBody CourseRequest request) {
        return courseService.getCourses(request);
    }

    @PostMapping("/add/section")
    public ResponseEntity<?> addSection(@Valid @RequestBody AddSectionRequest request) {
        return courseService.addSection(request);
    }

    @PostMapping("/delete/section")
    public ResponseEntity<?> removeSection(@Valid @RequestBody DeleteSectionRequest request) {
        return courseService.deleteSection(request);
    }

    @GetMapping("{id}/list/section")
    public List<SectionResponse> getSections(@PathVariable int id) {
        return courseService.listSections(id);
    }

    @GetMapping("/{course_id}/section/{section_id}")
    public ResponseEntity<?> getSection(@PathVariable int course_id, @PathVariable int section_id) {
        return courseService.getSectionOfCourse(course_id, section_id);
    }
}