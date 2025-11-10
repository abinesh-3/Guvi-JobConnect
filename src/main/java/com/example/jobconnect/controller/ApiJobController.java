package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.User;
import com.example.jobconnect.service.JobService;
import com.example.jobconnect.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class ApiJobController {

    private final JobService jobService;
    private final UserService userService;

    public ApiJobController(JobService jobService, UserService userService){
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping
    public List<Job> list(){ return jobService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
        return jobService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Job job, @AuthenticationPrincipal UserDetails ud){
        if (ud == null) return ResponseEntity.status(401).body("Unauthorized");
        User user = userService.findByEmail(ud.getUsername()).orElse(null);
        job.setEmployer(user);
        Job saved = jobService.save(job);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        jobService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
