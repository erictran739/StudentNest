// src/main/java/edu/csulb/cecs491b/studentnest/service/DepartmentService.java
package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.DepartmentResponse;
import edu.csulb.cecs491b.studentnest.entity.Course;
import edu.csulb.cecs491b.studentnest.entity.Department;
import edu.csulb.cecs491b.studentnest.entity.DepartmentChair;
import edu.csulb.cecs491b.studentnest.entity.Professor;
import edu.csulb.cecs491b.studentnest.entity.User;
import edu.csulb.cecs491b.studentnest.repository.CourseRepository;
import edu.csulb.cecs491b.studentnest.repository.DepartmentChairRepository;
import edu.csulb.cecs491b.studentnest.repository.DepartmentRepository;
import edu.csulb.cecs491b.studentnest.repository.ProfessorRepository;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departments;
    private final DepartmentChairRepository chairRepo;
    private final ProfessorRepository profRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;
    

    public List<DepartmentResponse> list() {
        return departments.findAll().stream().map(this::toResp).toList();
    } //

    public DepartmentResponse get(Long id) {
        Department d = departments.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));
        return toResp(d);
    }

    public DepartmentResponse create(CreateDepartmentRequest r) {
        if (r.name() == null || r.name().isBlank())
            throw new IllegalArgumentException("Department name is required");
        if (departments.existsByNameIgnoreCase(r.name()))
            throw new IllegalArgumentException("Department name already exists");

        Department d = new Department();
        d.setName(r.name().trim());
        d.setAbbreviation(r.abbreviation());
        d.setDescription(r.description());
        return toResp(departments.save(d));
    }//

    public DepartmentResponse update(Long id, UpdateDepartmentRequest r) {
        Department d = departments.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));

        if (r.name() != null && !r.name().isBlank()) d.setName(r.name().trim());
        if (r.abbreviation()!=null && !r.abbreviation().isBlank()) d.setAbbreviation(r.abbreviation());
        if (r.description() != null) d.setDescription(r.description());

        return toResp(departments.save(d));
    }

    public void delete(Long id) {
        if (!departments.existsById(id)) throw new IllegalArgumentException("Department not found: " + id);
        departments.deleteById(id);
    }

    /**
     One-to-one chair: ensure the selected chair points 
     to this department, and no other chair remains linked to it
     */
    public DepartmentResponse assignChair(Long deptId, Integer chairUserId) {
        Department d = departments.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + deptId));
        
        User u = userRepo.findById(chairUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + chairUserId));
        if (!(u instanceof DepartmentChair chair)) {
            throw new IllegalArgumentException("User " + chairUserId + " is not a DepartmentChair");
        }
        
     // If the department already has a chair, unlink it
        chairRepo.findByDepartment_Id(deptId)
                .ifPresent(existing -> { existing.setDepartment(null); chairRepo.save(existing); });

//        
//        DepartmentChair chairs = chairRepo.findById(chairUserId)
//                .orElseThrow(() -> new IllegalArgumentException("DepartmentChair not found: " + chairUserId));

        chair.setDepartment(d);
        chairRepo.save(chair);

        return toResp(d);
    }//

    /**
     * Remove chair assignment (sets chair.department = null)
     */
    public DepartmentResponse removeChair(Long deptId, Integer chairUserId) {
        DepartmentChair chair = chairRepo.findById(chairUserId)
                .orElseThrow(() -> new IllegalArgumentException("Chair user not found: " + chairUserId));

        if (chair.getDepartment()!=null && Objects.equals(chair.getDepartment().getId(), deptId)) {
            chair.setDepartment(null);
            chairRepo.save(chair);
        }
        return get(deptId);
    }

    public DepartmentResponse addProfessor(Long deptId, Integer professorUserId) {
        Department d = departments.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + deptId));
        Professor p = profRepo.findById(professorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found: " + professorUserId));
        p.setDepartment(d);
        profRepo.save(p);
        return toResp(d);
    }

    public DepartmentResponse removeProfessor(Long deptId, Integer professorUserId) {
        Professor p = profRepo.findById(professorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found: " + professorUserId));
        if (p.getDepartment()!=null && Objects.equals(p.getDepartment().getId(), deptId)) {
            p.setDepartment(null);
            profRepo.save(p);
        }
        return get(deptId);
    }

    public DepartmentResponse addCourse(Long deptId, Integer courseId) {
        Department d = departments.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + deptId));
        Course c = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        c.setDepartment(d);
        courseRepo.save(c);
        return toResp(d);
    }

    public DepartmentResponse removeCourse(Long deptId, Integer courseId) {
        Course c = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        if (c.getDepartment()!=null && Objects.equals(c.getDepartment().getId(), deptId)) {
            c.setDepartment(null);
            courseRepo.save(c);
        }
        return get(deptId);
    }

    private DepartmentResponse toResp(Department d) {
        Integer chairId = null;
        String chairName = null;

        // If you added Department.chair, you can read it directly; else query:
        var opt = chairRepo.findByDepartment_Id(d.getId());
        if (opt.isPresent()) {
            var ch = opt.get();
            chairId = ch.getUserID();
            chairName = (ch.getFirstName() + " " + ch.getLastName()).trim();
        }

        int professorCount = d.getProfessors() == null ? 0 : d.getProfessors().size();
        int courseCount    = d.getCourses()    == null ? 0 : d.getCourses().size();

        return new DepartmentResponse(
                d.getId(), d.getName(), d.getAbbreviation(), d.getDescription(),
                chairId, chairName, professorCount, courseCount
        );
    }
}