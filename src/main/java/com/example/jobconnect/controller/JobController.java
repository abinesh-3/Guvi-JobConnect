package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService){ this.jobService = jobService; }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        Job job = jobService.findById(id).orElse(null);
        model.addAttribute("job", job);
        return "jobs/detail";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required=false) String q, @RequestParam(required=false) String location, Model model){
        if (q != null && !q.isBlank()){
            model.addAttribute("jobs", jobService.searchByKeyword(q));
        } else if (location != null && !location.isBlank()){
            model.addAttribute("jobs", jobService.searchByLocation(location));
        } else {
            model.addAttribute("jobs", jobService.findAll());
        }
        return "jobs/list";
    }
}
