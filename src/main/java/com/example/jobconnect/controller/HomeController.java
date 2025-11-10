package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final JobService jobService;
    public HomeController(JobService jobService){ this.jobService = jobService; }

    @GetMapping({"/","/jobs"})
    public String index(Model model){
        List<Job> jobs = jobService.findAll();
        model.addAttribute("jobs", jobs);
        return "jobs/list";
    }
}
