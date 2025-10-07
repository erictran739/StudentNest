package edu.csulb.cecs491b.studentnest.repository;

import edu.csulb.cecs491b.studentnest.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository  extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
    boolean existsById(Long id);

    Student findByEmail(String reqEmail);
    Optional<Student> findById(long id);

}
