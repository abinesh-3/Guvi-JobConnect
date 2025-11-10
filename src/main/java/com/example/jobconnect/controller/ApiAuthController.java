package com.example.jobconnect.controller;

import com.example.jobconnect.dto.AuthResponse;
import com.example.jobconnect.dto.LoginRequest;
import com.example.jobconnect.model.Role;
import com.example.jobconnect.model.User;
import com.example.jobconnect.security.JwtUtils;
import com.example.jobconnect.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public ApiAuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User u, @RequestParam(defaultValue = "JOB_SEEKER") String role){
        if (u.getEmail() == null || u.getPassword() == null) return ResponseEntity.badRequest().body("Missing fields");
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        var roles = new HashSet<Role>();
        roles.add(Role.valueOf(role));
        u.setRoles(roles);
        userService.save(u);
        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        var opt = userService.findByEmail(req.email());
        if (opt.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
        var user = opt.get();
        if (!passwordEncoder.matches(req.password(), user.getPassword())) return ResponseEntity.status(401).body("Invalid credentials");
        String token = jwtUtils.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
    }
}
