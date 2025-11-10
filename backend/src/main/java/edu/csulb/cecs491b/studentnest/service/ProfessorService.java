package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.professor.ProfessorResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.entity.Professor;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.repository.ProfessorRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional

@AllArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final SectionRepository sectionRepository;

    public ResponseEntity<?> get(int userId){
        return ProfessorResponse.build(HttpStatus.OK, getProfessor(userId));
    }

    public List<ProfessorResponse> listAll(){
         return professorRepository.findAll().stream().map(ProfessorResponse::build).toList();
    }

    public List<SectionResponse> getSections(int userId){
        // Get professor
        Professor professor = getProfessor(userId);

        // Get list of sections, convert to stream, map as response, return
        return professor.getSections().stream().map(SectionResponse::build).toList();
    }


    // Helper Functions
    Professor getProfessor(int userId){
        return professorRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Professor with id [" + userId + "] not found")
        );
    }

}
