package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.User;
import com.example.jobconnect.service.JobService;
import com.example.jobconnect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployerControllerTest {

	@Mock JobService jobService;
	@Mock UserService userService;
	@Mock Model model;
	@InjectMocks EmployerController controller;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testDashboardAddsUserAndJobsToModel() {
		UserDetails ud = mock(UserDetails.class);
		when(ud.getUsername()).thenReturn("emp@example.com");
		User user = new User(); user.setEmail("emp@example.com");
		when(userService.findByEmail("emp@example.com")).thenReturn(Optional.of(user));
		List<Job> jobs = List.of(new Job(), new Job());
		when(jobService.findAll()).thenReturn(jobs);

		String view = controller.dashboard(ud, model);
		verify(model).addAttribute("user", user);
		verify(model).addAttribute("jobs", jobs);
		assertEquals("employer/dashboard", view);
	}

	@Test
	void testCreateFormAddsJobToModel() {
		when(model.addAttribute(eq("job"), any(Job.class))).thenReturn(model);
		String view = controller.createForm(model);
		verify(model).addAttribute(eq("job"), any(Job.class));
		assertEquals("employer/job_form", view);
	}

	@Test
	void testSaveJobSetsEmployerAndDeadline() {
		UserDetails ud = mock(UserDetails.class);
		when(ud.getUsername()).thenReturn("emp@example.com");
		User user = new User(); user.setEmail("emp@example.com");
		when(userService.findByEmail("emp@example.com")).thenReturn(Optional.of(user));
		Job job = new Job(); job.setTitle("Test Job"); job.setDeadline(null);
		when(jobService.save(any(Job.class))).thenReturn(job);

		String view = controller.saveJob(job, ud);
		assertEquals("redirect:/employer/dashboard", view);
		assertEquals(user, job.getEmployer());
		assertNotNull(job.getDeadline());
		verify(jobService).save(job);
	}

	@Test
	void testEditAddsJobToModel() {
		Job job = new Job(); job.setId(1L); job.setTitle("Edit Job");
		when(jobService.findById(1L)).thenReturn(Optional.of(job));
		when(model.addAttribute("job", job)).thenReturn(model);
		String view = controller.edit(1L, model);
		verify(model).addAttribute("job", job);
		assertEquals("employer/job_form", view);
	}

	@Test
	void testEditJobNotFoundAddsNewJobToModel() {
		when(jobService.findById(2L)).thenReturn(Optional.empty());
		when(model.addAttribute(eq("job"), any(Job.class))).thenReturn(model);
		String view = controller.edit(2L, model);
		verify(model).addAttribute(eq("job"), any(Job.class));
		assertEquals("employer/job_form", view);
	}

	@Test
	void testDeleteJobCallsServiceAndRedirects() {
		doNothing().when(jobService).deleteById(1L);
		String view = controller.delete(1L);
		assertEquals("redirect:/employer/dashboard", view);
		verify(jobService).deleteById(1L);
	}
}
