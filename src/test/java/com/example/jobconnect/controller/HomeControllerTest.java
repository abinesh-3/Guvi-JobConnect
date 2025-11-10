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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeControllerTest {

	@Mock JobService jobService;
	@Mock Model model;
	@InjectMocks HomeController controller;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testIndexAddsJobsToModelAndReturnsView() {
		List<Job> jobs = List.of(new Job(), new Job());
		when(jobService.findAll()).thenReturn(jobs);
		when(model.addAttribute("jobs", jobs)).thenReturn(model);

		String view = controller.index(model);
		verify(model).addAttribute("jobs", jobs);
		assertEquals("jobs/list", view);
	}
}
