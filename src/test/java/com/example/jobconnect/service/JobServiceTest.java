package com.example.jobconnect.service;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.User;
import com.example.jobconnect.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository repository;

    @InjectMocks
    private JobService service;

    private Job testJob;
    private User testEmployer;

    @BeforeEach
    void setUp() {
        testEmployer = new User();
        testEmployer.setId(1L);
        testEmployer.setEmail("employer@test.com");
        testEmployer.setFullName("Test Employer");

        testJob = new Job();
        testJob.setId(1L);
        testJob.setTitle("Senior Java Developer");
        testJob.setDescription("Java development position");
        testJob.setLocation("Remote");
        testJob.setSalary("$120,000");
        testJob.setDeadline(LocalDate.now().plusMonths(1));
        testJob.setEmployer(testEmployer);
    }

    @Test
    void testSaveJob() {
        
        when(repository.save(any(Job.class))).thenReturn(testJob);

     
        Job saved = service.save(testJob);

        
        assertNotNull(saved);
        assertEquals(testJob.getId(), saved.getId());
        assertEquals(testJob.getTitle(), saved.getTitle());
        assertEquals(testJob.getDescription(), saved.getDescription());
        assertEquals(testJob.getLocation(), saved.getLocation());
        assertEquals(testJob.getSalary(), saved.getSalary());
        assertEquals(testJob.getDeadline(), saved.getDeadline());
        assertEquals(testJob.getEmployer(), saved.getEmployer());

        
        verify(repository).save(testJob);
    }

    @Test
    void testFindAll() {
        
        List<Job> jobs = Arrays.asList(testJob);
        when(repository.findAll()).thenReturn(jobs);

        
        List<Job> found = service.findAll();

        
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(testJob.getId(), found.get(0).getId());
        assertEquals(testJob.getTitle(), found.get(0).getTitle());

        
        verify(repository).findAll();
    }

    @Test
    void testFindById() {
        
        when(repository.findById(1L)).thenReturn(Optional.of(testJob));

       
        Optional<Job> found = service.findById(1L);

        
        assertTrue(found.isPresent());
        assertEquals(testJob.getId(), found.get().getId());
        assertEquals(testJob.getTitle(), found.get().getTitle());

        
        verify(repository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        
        when(repository.findById(999L)).thenReturn(Optional.empty());

       
        Optional<Job> found = service.findById(999L);

        
        assertFalse(found.isPresent());

       
        verify(repository).findById(999L);
    }

    @Test
    void testDeleteById() {
        
        doNothing().when(repository).deleteById(1L);

        
        service.deleteById(1L);

        
        verify(repository).deleteById(1L);
    }

    @Test
    void testSearchByLocation() {
        
        when(repository.findByLocationContainingIgnoreCase("Remote"))
            .thenReturn(Arrays.asList(testJob));

        
        List<Job> found = service.searchByLocation("Remote");

        
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals("Remote", found.get(0).getLocation());

        
        verify(repository).findByLocationContainingIgnoreCase("Remote");
    }

    @Test
    void testSearchByLocationNoResults() {
        
        when(repository.findByLocationContainingIgnoreCase("Moon"))
            .thenReturn(Collections.emptyList());

        
        List<Job> found = service.searchByLocation("Moon");

       
        assertNotNull(found);
        assertTrue(found.isEmpty());

        
        verify(repository).findByLocationContainingIgnoreCase("Moon");
    }

    @Test
    void testSearchByKeyword() {
        
        when(repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Java", "Java"))
            .thenReturn(Arrays.asList(testJob));

        
        List<Job> found = service.searchByKeyword("Java");

        
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertTrue(found.get(0).getTitle().contains("Java") || 
                  found.get(0).getDescription().contains("Java"));

       
        verify(repository).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Java", "Java");
    }

    @Test
    void testSearchByKeywordNoResults() {
        
        when(repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("xyz", "xyz"))
            .thenReturn(Collections.emptyList());

        
        List<Job> found = service.searchByKeyword("xyz");

      
        assertNotNull(found);
        assertTrue(found.isEmpty());

       
        verify(repository).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("xyz", "xyz");
    }

    @Test
    void testSaveNull() {
       
        when(repository.save(null)).thenThrow(IllegalArgumentException.class);

     
        assertThrows(IllegalArgumentException.class, () -> service.save(null));

    
        verify(repository).save(null);
    }
}
