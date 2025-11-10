package com.example.jobconnect.controller;


import com.example.jobconnect.model.JobApplication;
import com.example.jobconnect.service.ApplicationService;
import com.example.jobconnect.service.JobService;
import com.example.jobconnect.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seeker")
public class SeekerController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserService userService;

    public SeekerController(JobService jobService, ApplicationService applicationService, UserService userService){
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @GetMapping("/apply/{jobId}")
    public String apply(@PathVariable Long jobId, @AuthenticationPrincipal UserDetails ud, Model model){
        var job = jobService.findById(jobId).orElse(null);
        var user = userService.findByEmail(ud.getUsername()).orElse(null);
        if (job == null || user == null) return "redirect:/jobs";
        JobApplication a = new JobApplication();
        a.setJob(job);
        a.setApplicant(user);
        applicationService.save(a);
        model.addAttribute("message", "Application submitted");
        return "redirect:/jobs/" + jobId;
    }
}
