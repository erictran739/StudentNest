package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.section.SectionResponse;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.repository.CourseRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional

@Getter
public class SectionService {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    SectionService(CourseRepository courseRepo, SectionRepository sectionRepo) {
        this.courseRepository = courseRepo;
        this.sectionRepository = sectionRepo;
    }


    // I prefer to return ResponseEntity's but in this case I'd rather use the map() function
    public List<SectionResponse> listSections() {
        return sectionRepository.findAll().stream().map(
                section -> new SectionResponse(
                        section.getCourse().getCourseID(),
                        section.getSectionID(),
                        section.getDepartment()
                )
        ).toList();
    }


    // Should use this in the listSections() function, verify course exist and other checks
    private SectionResponse multipleSectionsToResponse(Section s) {
        // Verify Course exists
        return new SectionResponse(s.getCourse().getCourseID(), s.getSectionID(), s.getDepartment());
    }

}
