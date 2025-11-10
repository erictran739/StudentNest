package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.professor.AssignSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.professor.ProfessorResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.student.UpdateStudentRequest;
import edu.csulb.cecs491b.studentnest.service.ProfessorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professors")

@AllArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        return professorService.get(id);
    }

    @GetMapping
    public List<ProfessorResponse> list() {
        return professorService.listAll();
    }

    @GetMapping("{id}/list/section")
    public List<SectionResponse> listSections(@PathVariable int id){
        return professorService.getSections(id);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateProfessor(@RequestBody UpdateStudentRequest request) {
        return null;
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignSection(@RequestBody AssignSectionRequest request){

        return null;
    }

//    @PostMapping("/enroll")
//    public ResponseEntity<?> enroll(@RequestBody EnrollSectionRequest req){
//        return studentService.enroll(req.student_id(), req.section_id());
//    }
//
//    @PostMapping("/drop")
//    public ResponseEntity<?> drop(@RequestBody DropSectionRequest req){
//        return studentService.drop(req.student_id(), req.section_id());
//    }
}

