package com.example.jobconnect.controller;

import com.example.jobconnect.model.Role;
import com.example.jobconnect.model.User;
import com.example.jobconnect.service.SmsService;
import com.example.jobconnect.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashSet;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, SmsService smsService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.smsService = smsService;
    }

    @GetMapping("/register")
    public String showRegister(Model model){
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult br, @RequestParam String role, Model model){
        if (br.hasErrors()) return "auth/register";
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var roles = new HashSet<Role>();
        if ("EMPLOYER".equalsIgnoreCase(role)) roles.add(Role.EMPLOYER);
        else roles.add(Role.JOB_SEEKER);
        user.setRoles(roles);
        userService.save(user);
        if (user.getPhone() != null) {
            smsService.sendSms(user.getPhone(), "Welcome to JobConnect, " + user.getFullName());
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String loginPage(){ return "auth/login"; }
}
