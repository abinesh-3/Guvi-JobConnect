package com.example.jobconnect.service;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.JobApplication;
import com.example.jobconnect.model.User;
import com.example.jobconnect.repository.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @InjectMocks
    private ApplicationService service;

    private JobApplication testApplication;
    private Job testJob;
    private User testApplicant;

    @BeforeEach
    void setUp() {
       
        testJob = new Job();
        testJob.setId(1L);
        testJob.setTitle("Software Engineer");

        testApplicant = new User();
        testApplicant.setId(1L);
        testApplicant.setEmail("applicant@test.com");

        testApplication = new JobApplication();
        testApplication.setId(1L);
        testApplication.setJob(testJob);
        testApplication.setApplicant(testApplicant);
        testApplication.setStatus("APPLIED");
        testApplication.setAppliedAt(LocalDateTime.now());
    }

    @Test
    void testSaveApplication() {
      
        when(repository.save(any(JobApplication.class))).thenReturn(testApplication);

        
        JobApplication saved = service.save(testApplication);

        
        assertNotNull(saved);
        assertEquals(testApplication.getId(), saved.getId());
        assertEquals(testApplication.getJob(), saved.getJob());
        assertEquals(testApplication.getApplicant(), saved.getApplicant());
        assertEquals(testApplication.getStatus(), saved.getStatus());
        
      
        verify(repository).save(testApplication);
    }

    @Test
    void testFindByJob() {
       
        List<JobApplication> applications = Arrays.asList(testApplication);
        when(repository.findByJob(testJob)).thenReturn(applications);

        
        List<JobApplication> found = service.findByJob(testJob);

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testApplication.getId(), found.get(0).getId());
        assertEquals(testApplication.getStatus(), found.get(0).getStatus());
        
       
        verify(repository).findByJob(testJob);
    }

    @Test
    void testFindByJobWithNoApplications() {
       
        when(repository.findByJob(testJob)).thenReturn(Arrays.asList());

        
        List<JobApplication> found = service.findByJob(testJob);

        
        assertNotNull(found);
        assertTrue(found.isEmpty());
        
        
        verify(repository).findByJob(testJob);
    }

    @Test
    void testFindById() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(testApplication));

        
        Optional<JobApplication> found = service.findById(1L);

        
        assertTrue(found.isPresent());
        assertEquals(testApplication.getId(), found.get().getId());
        assertEquals(testApplication.getJob(), found.get().getJob());
        assertEquals(testApplication.getApplicant(), found.get().getApplicant());
        assertEquals(testApplication.getStatus(), found.get().getStatus());
        
        
        verify(repository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
       
        when(repository.findById(999L)).thenReturn(Optional.empty());

        
        Optional<JobApplication> found = service.findById(999L);

       
        assertFalse(found.isPresent());
        
       
        verify(repository).findById(999L);
    }

    @Test
    void testSaveWithNullApplication() {
        // Arrange
        when(repository.save(null)).thenThrow(IllegalArgumentException.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        
        // Verify
        verify(repository).save(null);
    }

    @Test
    void testFindByJobWithNullJob() {
        // Arrange
        when(repository.findByJob(null)).thenReturn(Arrays.asList());

        // Act
        List<JobApplication> found = service.findByJob(null);

        // Assert
        assertNotNull(found);
        assertTrue(found.isEmpty());
        
        // Verify
        verify(repository).findByJob(null);
    }
}
