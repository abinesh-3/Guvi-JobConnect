package com.example.jobconnect.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JobApplicationTest {

	@Mock
	private Job mockJob;

	@Mock
	private User mockApplicant;

	private JobApplication application;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		application = new JobApplication();
	}

	@Test
	void testDefaultState() {
		assertNull(application.getId(), "New application should have null id");
		assertNull(application.getJob(), "Job should be null by default");
		assertNull(application.getApplicant(), "Applicant should be null by default");
		assertNotNull(application.getAppliedAt(), "appliedAt should be initialized");
		assertEquals("APPLIED", application.getStatus(), "Default status should be APPLIED");

		
		LocalDateTime now = LocalDateTime.now();
		assertFalse(application.getAppliedAt().isAfter(now.plusSeconds(5)), "appliedAt should not be in the future");
		assertFalse(application.getAppliedAt().isBefore(now.minusMinutes(1)), "appliedAt should be recent");
	}

	@Test
	void testSettersAndGetters() {
		application.setId(100L);
		application.setJob(mockJob);
		application.setApplicant(mockApplicant);

		LocalDateTime custom = LocalDateTime.of(2025, 1, 1, 12, 0);
		application.setAppliedAt(custom);
		application.setStatus("ACCEPTED");

		assertEquals(100L, application.getId());
		assertSame(mockJob, application.getJob());
		assertSame(mockApplicant, application.getApplicant());
		assertEquals(custom, application.getAppliedAt());
		assertEquals("ACCEPTED", application.getStatus());
	}

	@Test
	void testMockedJobAndApplicantInteraction() {
		when(mockJob.getTitle()).thenReturn("Mockito Job Title");
		when(mockApplicant.getEmail()).thenReturn("applicant@test.com");

		application.setJob(mockJob);
		application.setApplicant(mockApplicant);

		
		assertEquals("Mockito Job Title", application.getJob().getTitle());
		assertEquals("applicant@test.com", application.getApplicant().getEmail());

		
		verify(mockJob, times(1)).getTitle();
		verify(mockApplicant, times(1)).getEmail();
	}

	@Test
	void testStatusTransitionsAndNulls() {
		
		application.setStatus("REJECTED");
		assertEquals("REJECTED", application.getStatus());

		application.setStatus(null);
		assertNull(application.getStatus(), "status may be set to null (no validation present)");

		
		application.setJob(null);
		application.setApplicant(null);
		assertNull(application.getJob());
		assertNull(application.getApplicant());
	}

	@Test
	void testAppliedAtMutability() {
		LocalDateTime before = application.getAppliedAt();
		LocalDateTime later = before.plus(Duration.ofDays(7));
		application.setAppliedAt(later);
		assertEquals(later, application.getAppliedAt());
	}
}
