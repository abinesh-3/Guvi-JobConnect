package com.example.jobconnect.controller;

import com.example.jobconnect.model.Role;
import com.example.jobconnect.model.User;
import com.example.jobconnect.service.SmsService;
import com.example.jobconnect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

	@Mock UserService userService;
	@Mock PasswordEncoder passwordEncoder;
	@Mock SmsService smsService;
	@Mock Model model;
	@Mock BindingResult bindingResult;
	@InjectMocks AuthController controller;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testShowRegisterAddsUserToModel() {
		when(model.addAttribute(eq("user"), any(User.class))).thenReturn(model);
		String view = controller.showRegister(model);
		verify(model).addAttribute(eq("user"), any(User.class));
		assertEquals("auth/register", view);
	}

	@Test
	void testRegisterWithErrorsReturnsRegisterView() {
		when(bindingResult.hasErrors()).thenReturn(true);
		User user = new User();
		String view = controller.register(user, bindingResult, "JOB_SEEKER", model);
		assertEquals("auth/register", view);
		verify(bindingResult).hasErrors();
	}

	@Test
	void testRegisterEmployerSuccess() {
		when(bindingResult.hasErrors()).thenReturn(false);
		User user = new User();
		user.setPassword("plain");
		user.setPhone("1234567890");
		user.setFullName("Emp");
		when(passwordEncoder.encode("plain")).thenReturn("encoded");
		when(userService.save(any(User.class))).thenReturn(user);

		String view = controller.register(user, bindingResult, "EMPLOYER", model);
		assertEquals("redirect:/login?registered", view);
		assertEquals("encoded", user.getPassword());
		assertTrue(user.getRoles().contains(Role.EMPLOYER));
		verify(smsService).sendSms(eq("1234567890"), contains("Welcome to JobConnect"));
		verify(userService).save(user);
	}

	@Test
	void testRegisterJobSeekerSuccessNoPhone() {
		when(bindingResult.hasErrors()).thenReturn(false);
		User user = new User();
		user.setPassword("plain");
		user.setFullName("Seeker");
		user.setPhone(null);
		when(passwordEncoder.encode("plain")).thenReturn("encoded");
		when(userService.save(any(User.class))).thenReturn(user);

		String view = controller.register(user, bindingResult, "JOB_SEEKER", model);
		assertEquals("redirect:/login?registered", view);
		assertEquals("encoded", user.getPassword());
		assertTrue(user.getRoles().contains(Role.JOB_SEEKER));
		verify(userService).save(user);
		verify(smsService, never()).sendSms(anyString(), anyString());
	}

	@Test
	void testLoginPageReturnsLoginView() {
		String view = controller.loginPage();
		assertEquals("auth/login", view);
	}
}
