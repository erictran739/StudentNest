package edu.csulb.cecs491b.studentnest.repository;

import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SectionRepository extends JpaRepository<Section, Integer> {
    List<Section> findAllByCourseIs(Course course);
}