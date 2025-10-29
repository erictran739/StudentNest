package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.course.AddSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.course.CreateCourseRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.service.CourseService;
import edu.csulb.cecs491b.studentnest.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final CourseService courseService;
    private final SectionService sectionService;

    public SectionController(CourseService service, SectionService sectionService) {
        this.courseService = service;
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<SectionResponse> listSections(){
        return sectionService.listSections();
    }

    @GetMapping("/{id}")
    public SectionResponse getSection(@PathVariable String id){
        return null;
    }

//    @PostMapping("/create") public ResponseEntity<?> getSection(@PathVariable){
//        return null;
//    }

}