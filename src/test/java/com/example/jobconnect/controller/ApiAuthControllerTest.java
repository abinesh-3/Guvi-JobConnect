
package com.example.jobconnect.controller;

import com.example.jobconnect.dto.LoginRequest;
import com.example.jobconnect.model.Role;
import com.example.jobconnect.model.User;
import com.example.jobconnect.security.JwtUtils;
import com.example.jobconnect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiAuthControllerTest {

    @Mock UserService userService;
    @Mock JwtUtils jwtUtils;
    @Mock org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @InjectMocks ApiAuthController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("Tester");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userService.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = controller.register(user, "JOB_SEEKER");
    assertEquals(200, response.getStatusCode().value());
        assertEquals("Registered", response.getBody());
        verify(userService).save(any(User.class));
    }

    @Test
    void testRegisterMissingFields() {
        User user = new User();
        user.setEmail(null);
        user.setPassword(null);
        ResponseEntity<?> response = controller.register(user, "JOB_SEEKER");
    assertEquals(400, response.getStatusCode().value());
        assertEquals("Missing fields", response.getBody());
    }

    @Test
    void testLoginSuccess() {
        LoginRequest req = mock(LoginRequest.class);
        when(req.email()).thenReturn("test@example.com");
        when(req.password()).thenReturn("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.JOB_SEEKER);
        user.setRoles(roles);

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtils.generateToken("test@example.com")).thenReturn("jwt-token");

        ResponseEntity<?> response = controller.login(req);
    assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("jwt-token"));
    }

    @Test
    void testLoginInvalidEmail() {
        LoginRequest req = mock(LoginRequest.class);
        when(req.email()).thenReturn("wrong@example.com");
        when(userService.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.login(req);
    assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void testLoginInvalidPassword() {
        LoginRequest req = mock(LoginRequest.class);
        when(req.email()).thenReturn("test@example.com");
        when(req.password()).thenReturn("wrongpassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        ResponseEntity<?> response = controller.login(req);
    assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody());
    }
}
