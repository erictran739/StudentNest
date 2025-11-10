package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.repository.CourseRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional

@Getter
@AllArgsConstructor
public class SectionService {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    // I prefer to return ResponseEntity's but in this case I'd rather use the map() function
    public List<SectionResponse> listSections() {
        return sectionRepository.findAll().stream().map(SectionResponse::build).toList();
    }
}
