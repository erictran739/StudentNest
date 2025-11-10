// src/main/java/edu/csulb/cecs491b/studentnest/controller/DepartmentController.java
package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.DepartmentResponse;
import edu.csulb.cecs491b.studentnest.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/departments")

@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping
    public List<DepartmentResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public DepartmentResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@RequestBody CreateDepartmentRequest req) {
        var saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/departments/" + saved.id()))
                .body(saved);
    }

    @PatchMapping("/{id}")
    public DepartmentResponse patch(@PathVariable Long id,
                                    @RequestBody UpdateDepartmentRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // -------- Chair linking --------
    @PutMapping("/{id}/chair")
    public DepartmentResponse assignChair(@PathVariable Long id,
                                          @RequestParam("chairUserId") Integer chairUserId) {
        return service.assignChair(id, chairUserId);
    }


    @DeleteMapping("/{id}/chair")
    public DepartmentResponse removeChair(@PathVariable Long id,
                                          @RequestParam("chairUserId") Integer chairUserId) {
        return service.removeChair(id, chairUserId);
    }

    // Professors
    @PutMapping("/{id}/professors/{profUserId}")
    public DepartmentResponse addProfessor(@PathVariable Long id, @PathVariable Integer profUserId) {
        return service.addProfessor(id, profUserId);
    }

    @DeleteMapping("/{id}/professors/{profUserId}")
    public DepartmentResponse removeProfessor(@PathVariable Long id, @PathVariable Integer profUserId) {
        return service.removeProfessor(id, profUserId);
    }

    // Courses // rest logic /api/departments/{deptId}/courses/{courseId} this mean
    //modify the department's course list
    @PutMapping("/{id}/courses/{courseId}")
    public DepartmentResponse addCourse(@PathVariable Long id, @PathVariable Integer courseId) {
        return service.addCourse(id, courseId);
    }

    @DeleteMapping("/{id}/courses/{courseId}")
    public DepartmentResponse removeCourse(@PathVariable Long id, @PathVariable Integer courseId) {
        return service.removeCourse(id, courseId);
    }
}

