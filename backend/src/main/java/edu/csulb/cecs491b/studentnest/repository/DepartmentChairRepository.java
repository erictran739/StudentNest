package edu.csulb.cecs491b.studentnest.repository;

import edu.csulb.cecs491b.studentnest.entity.DepartmentChair;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentChairRepository extends JpaRepository<DepartmentChair, Integer> {
    Optional<DepartmentChair> findByDepartment_Id(Long deptId); // One-to-one => Optional
}
