package edu.csulb.cecs491b.studentnest.controller;

import edu.csulb.cecs491b.studentnest.controller.dto.section.DropSectionRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.EnrollRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.section.EnrollResponse;
import edu.csulb.cecs491b.studentnest.controller.dto.user.CreateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UpdateUserRequest;
import edu.csulb.cecs491b.studentnest.controller.dto.user.UserResponse;
import edu.csulb.cecs491b.studentnest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping
    public List<UserResponse> list() { return service.list(); }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable int id) { return service.get(id); }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        UserResponse saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/users/" + saved.userID())).body(saved);
    }

//    @PutMapping("/{id}")
//    public UserResponse update(@PathVariable int id, @Valid @RequestBody UpdateUserRequest req) {
//        return service.update(id, req);
//    }
    
    @PatchMapping("/{id}")
    public UserResponse patch(@PathVariable int id, @RequestBody UpdateUserRequest req) {
    	return service.partialUpdate(id, req);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@RequestBody EnrollRequest req){
        return service.enroll(req.user_id(), req.section_id());
    }

    @PostMapping("/drop")
    public ResponseEntity<?> drop(@RequestBody DropSectionRequest req){
        return service.drop(req.user_id(), req.section_id());
    }
}
