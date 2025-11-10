package com.example.jobconnect.service;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.repository.JobRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private final JobRepository repo;
    public JobService(JobRepository repo) { this.repo = repo; }

    public Job save(Job j){ return repo.save(j); }
    public List<Job> findAll(){ return repo.findAll(); }
    public Optional<Job> findById(Long id){ return repo.findById(id); }
    public void deleteById(Long id){ repo.deleteById(id); }
    public List<Job> searchByLocation(String location){ return repo.findByLocationContainingIgnoreCase(location); }
    public List<Job> searchByKeyword(String keyword){ return repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword); }
}
