package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.service.JobService;
import com.example.jobconnect.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final JobService jobService;
    private final UserService userService;

    public EmployerController(JobService jobService, UserService userService){
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails ud, Model model){
        var user = userService.findByEmail(ud.getUsername()).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("jobs", jobService.findAll());
        return "employer/dashboard";
    }

    @GetMapping("/jobs/new")
    public String createForm(Model model){
        model.addAttribute("job", new Job());
        return "employer/job_form";
    }

    @PostMapping("/jobs")
    public String saveJob(@ModelAttribute Job job, @AuthenticationPrincipal UserDetails ud){
        var user = userService.findByEmail(ud.getUsername()).orElse(null);
        job.setEmployer(user);
        if (job.getDeadline() == null) job.setDeadline(LocalDate.now().plusWeeks(2));
        jobService.save(job);
        return "redirect:/employer/dashboard";
    }

    @GetMapping("/jobs/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        var job = jobService.findById(id).orElse(new Job());
        model.addAttribute("job", job);
        return "employer/job_form";
    }

    @PostMapping("/jobs/delete/{id}")
    public String delete(@PathVariable Long id){
        jobService.deleteById(id);
        return "redirect:/employer/dashboard";
    }
}
