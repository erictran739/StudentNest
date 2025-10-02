package edu.csulb.cecs491b.studentnest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.csulb.cecs491b.studentnest.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
