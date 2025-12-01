package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.entity.Enrollment;
import edu.csulb.cecs491b.studentnest.entity.EnrollmentID;
import edu.csulb.cecs491b.studentnest.entity.Section;
import edu.csulb.cecs491b.studentnest.entity.Student;
import edu.csulb.cecs491b.studentnest.repository.EnrollmentRepository;
import edu.csulb.cecs491b.studentnest.repository.SectionRepository;
import edu.csulb.cecs491b.studentnest.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            SectionRepository sectionRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.sectionRepository = sectionRepository;
    }

    /** GET enrolled classes */
    public ResponseEntity<?> getEnrollmentsByUserId(int userId) {
        List<Enrollment> enrollments = enrollmentRepository.findAllByEnrollmentID_UserID(userId);
        return ResponseEntity.ok(enrollments);
    }

    /** ENROLL student in a section */
    public ResponseEntity<?> enroll(int studentId, int sectionId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Section section = sectionRepository.findById(sectionId).orElse(null);

        if (student == null || section == null) {
            return ResponseEntity.badRequest().body("Invalid Student ID or Section ID");
        }

        EnrollmentID id = new EnrollmentID(studentId, sectionId);

        if (enrollmentRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Already enrolled in section " + sectionId);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentID(id);
        enrollment.setStudent(student);
        enrollment.setSection(section);
        enrollment.setEnrollmentDate(LocalDate.now().toString());
        enrollment.setGrade(' ');

        enrollmentRepository.save(enrollment);

        return ResponseEntity.ok("Enrolled in section " + sectionId);
    }

    /** DROP a class */
    public ResponseEntity<?> drop(int studentId, int sectionId) {
        EnrollmentID id = new EnrollmentID(studentId, sectionId);

        if (!enrollmentRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Not enrolled in section " + sectionId);
        }

        enrollmentRepository.deleteById(id);
        return ResponseEntity.ok("Dropped section " + sectionId);
    }
}