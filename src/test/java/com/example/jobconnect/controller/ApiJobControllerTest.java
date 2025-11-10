
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiJobControllerTest {

    @Mock JobService jobService;
    @Mock UserService userService;
    @InjectMocks ApiJobController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListJobs() {
        Job job1 = new Job(); job1.setTitle("Job1");
        Job job2 = new Job(); job2.setTitle("Job2");
        when(jobService.findAll()).thenReturn(List.of(job1, job2));
        List<Job> jobs = controller.list();
        assertEquals(2, jobs.size());
        assertEquals("Job1", jobs.get(0).getTitle());
    }

    @Test
    void testDetailFound() {
        Job job = new Job(); job.setId(1L); job.setTitle("Job1");
        when(jobService.findById(1L)).thenReturn(Optional.of(job));
        ResponseEntity<?> resp = controller.detail(1L);
        assertEquals(200, resp.getStatusCode().value());
        assertEquals(job, resp.getBody());
    }

    @Test
    void testDetailNotFound() {
        when(jobService.findById(2L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = controller.detail(2L);
        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    void testCreateJobAuthorized() {
        Job job = new Job(); job.setTitle("New Job");
        User employer = new User(); employer.setEmail("emp@example.com");
        UserDetails ud = mock(UserDetails.class);
        when(ud.getUsername()).thenReturn("emp@example.com");
        when(userService.findByEmail("emp@example.com")).thenReturn(Optional.of(employer));
        when(jobService.save(any(Job.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = controller.create(job, ud);
        assertEquals(200, resp.getStatusCode().value());
        Job saved = (Job) resp.getBody();
        assertEquals("New Job", saved.getTitle());
        assertEquals(employer, saved.getEmployer());
    }

    @Test
    void testCreateJobUnauthorized() {
        Job job = new Job(); job.setTitle("New Job");
        ResponseEntity<?> resp = controller.create(job, null);
        assertEquals(401, resp.getStatusCode().value());
        assertEquals("Unauthorized", resp.getBody());
    }

    @Test
    void testDeleteJob() {
        doNothing().when(jobService).deleteById(1L);
        ResponseEntity<?> resp = controller.delete(1L);
        assertEquals(200, resp.getStatusCode().value());
        verify(jobService).deleteById(1L);
    }
}
