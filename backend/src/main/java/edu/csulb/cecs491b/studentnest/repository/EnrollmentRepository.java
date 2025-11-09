package edu.csulb.cecs491b.studentnest.repository;

import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.entity.EnrollmentID;
import edu.csulb.cecs491b.studentnest.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentID> {
    List<Enrollment> findAllBySectionIs(Section section);
}