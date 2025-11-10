package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.service.CourseService;
import edu.csulb.cecs491b.studentnest.service.SectionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections")

@AllArgsConstructor
public class SectionController {

    private final CourseService courseService;
    private final SectionService sectionService;

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