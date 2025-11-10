package com.example.jobconnect.service;

import com.example.jobconnect.model.JobApplication;
import com.example.jobconnect.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final JobApplicationRepository repo;
    public ApplicationService(JobApplicationRepository repo){ this.repo = repo; }

    public JobApplication save(JobApplication a){ return repo.save(a); }
    public List<JobApplication> findByJob(com.example.jobconnect.model.Job job){ return repo.findByJob(job); }
    public Optional<JobApplication> findById(Long id){ return repo.findById(id); }
}
