// src/main/java/edu/csulb/cecs491b/studentnest/controller/DepartmentController.java
package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.CreateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.UpdateDepartmentRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.DepartmentResponse;
import edu.csulb.cecs491b.studentnest.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    
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
}
