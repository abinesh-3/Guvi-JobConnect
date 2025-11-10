
package com.example.jobconnect.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import java.time.LocalDate;

public class JobTest {
    @Mock
    private User mockEmployer;

    private Job job;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        job = new Job();
    }

    @Test
    void testJobCreation() {
        assertNotNull(job);
        assertNull(job.getId());
        assertNull(job.getTitle());
        assertNull(job.getDescription());
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        job.setId(id);
        assertEquals(id, job.getId());
    }

    @Test
    void testSetAndGetTitle() {
        String title = "Software Engineer";
        job.setTitle(title);
        assertEquals(title, job.getTitle());
    }

    @Test
    void testSetAndGetDescription() {
        String description = "Job description here";
        job.setDescription(description);
        assertEquals(description, job.getDescription());
    }

    @Test
    void testSetAndGetLocation() {
        String location = "New York";
        job.setLocation(location);
        assertEquals(location, job.getLocation());
    }

    @Test
    void testSetAndGetSalary() {
        String salary = "$100,000";
        job.setSalary(salary);
        assertEquals(salary, job.getSalary());
    }

    @Test
    void testSetAndGetDeadline() {
        LocalDate deadline = LocalDate.now().plusDays(30);
        job.setDeadline(deadline);
        assertEquals(deadline, job.getDeadline());
    }

    @Test
    void testSetAndGetEmployer() {
        when(mockEmployer.getEmail()).thenReturn("employer@test.com");
        job.setEmployer(mockEmployer);
        
        assertNotNull(job.getEmployer());
        assertEquals("employer@test.com", job.getEmployer().getEmail());
        assertSame(mockEmployer, job.getEmployer());
    }

    @Test
    void testCompleteJobCreation() {
        Long id = 1L;
        String title = "Full Stack Developer";
        String description = "Building web applications";
        String location = "Remote";
        String salary = "$120,000";
        LocalDate deadline = LocalDate.now().plusMonths(1);

        job.setId(id);
        job.setTitle(title);
        job.setDescription(description);
        job.setLocation(location);
        job.setSalary(salary);
        job.setDeadline(deadline);
        job.setEmployer(mockEmployer);

        assertEquals(id, job.getId());
        assertEquals(title, job.getTitle());
        assertEquals(description, job.getDescription());
        assertEquals(location, job.getLocation());
        assertEquals(salary, job.getSalary());
        assertEquals(deadline, job.getDeadline());
        assertSame(mockEmployer, job.getEmployer());
    }

    @Test
    void testNullValues() {
        job.setTitle(null);
        job.setDescription(null);
        job.setLocation(null);
        job.setSalary(null);
        job.setDeadline(null);
        job.setEmployer(null);

        assertNull(job.getTitle());
        assertNull(job.getDescription());
        assertNull(job.getLocation());
        assertNull(job.getSalary());
        assertNull(job.getDeadline());
        assertNull(job.getEmployer());
    }

    @Test
    void testMockEmployerBehavior() {
        when(mockEmployer.getEmail()).thenReturn("employer@test.com");
        when(mockEmployer.getFullName()).thenReturn("Test Employer");
        
        job.setEmployer(mockEmployer);
        
        assertEquals("employer@test.com", job.getEmployer().getEmail());
        assertEquals("Test Employer", job.getEmployer().getFullName());
        verify(mockEmployer, times(1)).getEmail();
        verify(mockEmployer, times(1)).getFullName();
    }

        @Test
        void testWithMockito() {
            Job mockJob = Mockito.mock(Job.class);
            User user = Mockito.mock(User.class);
            when(mockJob.getId()).thenReturn(202L);
            when(mockJob.getTitle()).thenReturn("QA");
            when(mockJob.getEmployer()).thenReturn(user);
            assertEquals(202L, mockJob.getId());
            assertEquals("QA", mockJob.getTitle());
            assertSame(user, mockJob.getEmployer());
        }
}

