package edu.csulb.cecs491b.studentnest.repository;

import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.entity.EnrollmentID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentID> {
}