package edu.csulb.cecs491b.studentnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.csulb.cecs491b.studentnest.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
	
}

