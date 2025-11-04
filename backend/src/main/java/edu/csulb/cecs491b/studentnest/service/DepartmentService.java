// src/main/java/edu/csulb/cecs491b/studentnest/service/DepartmentService.java
package edu.csulb.cecs491b.studentnest.service;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.DepartmentResponse;
import edu.csulb.cecs491b.studentnest.entity.Department;
import edu.csulb.cecs491b.studentnest.entity.DepartmentChair;
import edu.csulb.cecs491b.studentnest.repository.DepartmentChairRepository;
import edu.csulb.cecs491b.studentnest.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departments;
    private final DepartmentChairRepository chairs;

    public DepartmentService(DepartmentRepository departments, DepartmentChairRepository chairs) {
        this.departments = departments;
        this.chairs = chairs;
    }

    public List<DepartmentResponse> list() {
        return departments.findAll().stream().map(this::toResponse).toList();
    }

    public DepartmentResponse get(Long id) {
        Department d = departments.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));
        return toResponse(d);
    }

    public DepartmentResponse create(CreateDepartmentRequest r) {
        if (r.name() == null || r.name().isBlank())
            throw new IllegalArgumentException("Department name is required");
        if (departments.existsByNameIgnoreCase(r.name()))
            throw new IllegalArgumentException("Department name already exists");

        Department d = new Department();
        d.setName(r.name().trim());
        d.setDescription(r.description());
        return toResponse(departments.save(d));
    }

    public DepartmentResponse update(Long id, UpdateDepartmentRequest r) {
        Department d = departments.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));

        if (r.name() != null && !r.name().isBlank()) d.setName(r.name().trim());
        if (r.description() != null) d.setDescription(r.description());

        return toResponse(departments.save(d));
    }

    public void delete(Long id) {
        if (!departments.existsById(id)) throw new IllegalArgumentException("Department not found: " + id);
        departments.deleteById(id);
    }

    /**
     * Assign a chair to this department (ManyToOne on your entity).
     * This sets chair.department = this department. It DOES NOT clear other chairs for this dept.
     * Business rule (single chair) can be enforced by first clearing any other chairs if you want.
     */
    public DepartmentResponse assignChair(Long deptId, Integer chairUserId) {
        Department d = departments.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + deptId));

        DepartmentChair c = chairs.findById(chairUserId)
                .orElseThrow(() -> new IllegalArgumentException("DepartmentChair not found: " + chairUserId));

        c.setDepartment(d);
        chairs.save(c);

        return toResponse(d);
    }

    /**
     * Remove chair assignment (sets chair.department = null)
     */
    public DepartmentResponse removeChair(Long deptId, Integer chairUserId) {
        Department d = departments.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + deptId));

        DepartmentChair c = chairs.findById(chairUserId)
                .orElseThrow(() -> new IllegalArgumentException("DepartmentChair not found: " + chairUserId));

        if (c.getDepartment() != null && d.getId().equals(c.getDepartment().getId())) {
            c.setDepartment(null);
            chairs.save(c);
        }
        return toResponse(d);
    }

    private DepartmentResponse toResponse(Department d) {
        // Since Department doesn't have a `chair` field, pick one chair (e.g., latest id) for display.
        var chair = chairs.findAll().stream()
                .filter(c -> c.getDepartment() != null && d.getId().equals(c.getDepartment().getId()))
                .max(Comparator.comparing(DepartmentChair::getUserID)) // just pick the highest id as "current"
                .orElse(null);

        Integer chairId = chair == null ? null : chair.getUserID();
        String chairName = chair == null ? null : (chair.getFirstName() + " " + chair.getLastName()).trim();

        return new DepartmentResponse(
                d.getId(),
                d.getName(),
                d.getDescription(),
                chairId,
                chairName
        );
    }
}
