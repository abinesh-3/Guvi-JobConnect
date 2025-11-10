package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobControllerTest {

	@Mock JobService jobService;
	@Mock Model model;
	@InjectMocks JobController controller;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testDetailAddsJobToModelAndReturnsView() {
		Job job = new Job(); job.setId(1L); job.setTitle("Test Job");
		when(jobService.findById(1L)).thenReturn(Optional.of(job));
		when(model.addAttribute("job", job)).thenReturn(model);

		String view = controller.detail(1L, model);
		verify(model).addAttribute("job", job);
		assertEquals("jobs/detail", view);
	}

	@Test
	void testDetailJobNotFoundAddsNullToModel() {
		when(jobService.findById(2L)).thenReturn(Optional.empty());
		when(model.addAttribute("job", null)).thenReturn(model);

		String view = controller.detail(2L, model);
		verify(model).addAttribute("job", null);
		assertEquals("jobs/detail", view);
	}

	@Test
	void testSearchByKeyword() {
		List<Job> jobs = List.of(new Job(), new Job());
		when(jobService.searchByKeyword("dev")).thenReturn(jobs);
		when(model.addAttribute("jobs", jobs)).thenReturn(model);

		String view = controller.search("dev", null, model);
		verify(model).addAttribute("jobs", jobs);
		assertEquals("jobs/list", view);
	}

	@Test
	void testSearchByLocation() {
		List<Job> jobs = List.of(new Job());
		when(jobService.searchByLocation("NYC")).thenReturn(jobs);
		when(model.addAttribute("jobs", jobs)).thenReturn(model);

		String view = controller.search(null, "NYC", model);
		verify(model).addAttribute("jobs", jobs);
		assertEquals("jobs/list", view);
	}

	@Test
	void testSearchNoParamsReturnsAllJobs() {
		List<Job> jobs = List.of(new Job());
		when(jobService.findAll()).thenReturn(jobs);
		when(model.addAttribute("jobs", jobs)).thenReturn(model);

		String view = controller.search(null, null, model);
		verify(model).addAttribute("jobs", jobs);
		assertEquals("jobs/list", view);
	}
}
