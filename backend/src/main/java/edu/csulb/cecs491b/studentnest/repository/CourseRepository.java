package edu.csulb.cecs491b.studentnest.repository;

import edu.csulb.cecs491b.studentnest.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Integer> {
//    List<Course> findAllByDepartment();
}