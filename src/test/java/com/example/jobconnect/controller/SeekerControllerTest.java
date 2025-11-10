package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.JobApplication;
import com.example.jobconnect.model.User;
import com.example.jobconnect.service.ApplicationService;
import com.example.jobconnect.service.JobService;
import com.example.jobconnect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeekerControllerTest {

	@Mock JobService jobService;
	@Mock ApplicationService applicationService;
	@Mock UserService userService;
	@Mock Model model;
	@InjectMocks SeekerController controller;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testApplySuccess() {
		UserDetails ud = mock(UserDetails.class);
		when(ud.getUsername()).thenReturn("seeker@example.com");
		Job job = new Job(); job.setId(1L);
		User user = new User(); user.setEmail("seeker@example.com");
		when(jobService.findById(1L)).thenReturn(Optional.of(job));
		when(userService.findByEmail("seeker@example.com")).thenReturn(Optional.of(user));
		when(applicationService.save(any(JobApplication.class))).thenAnswer(inv -> inv.getArgument(0));
		when(model.addAttribute(eq("message"), anyString())).thenReturn(model);

		String view = controller.apply(1L, ud, model);
		verify(applicationService).save(any(JobApplication.class));
		verify(model).addAttribute(eq("message"), eq("Application submitted"));
		assertEquals("redirect:/jobs/1", view);
	}

	@Test
	void testApplyJobNotFoundRedirects() {
		UserDetails ud = mock(UserDetails.class);
		when(ud.getUsername()).thenReturn("seeker@example.com");
		when(jobService.findById(2L)).thenReturn(Optional.empty());
		User user = new User(); user.setEmail("seeker@example.com");
		when(userService.findByEmail("seeker@example.com")).thenReturn(Optional.of(user));

		String view = controller.apply(2L, ud, model);
		assertEquals("redirect:/jobs", view);
		verify(applicationService, never()).save(any(JobApplication.class));
	}

	@Test
	void testApplyUserNotFoundRedirects() {
		UserDetails ud = mock(UserDetails.class);
		when(ud.getUsername()).thenReturn("seeker@example.com");
		Job job = new Job(); job.setId(3L);
		when(jobService.findById(3L)).thenReturn(Optional.of(job));
		when(userService.findByEmail("seeker@example.com")).thenReturn(Optional.empty());

		String view = controller.apply(3L, ud, model);
		assertEquals("redirect:/jobs", view);
		verify(applicationService, never()).save(any(JobApplication.class));
	}
}
