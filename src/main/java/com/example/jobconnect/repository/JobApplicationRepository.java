package com.example.jobconnect.repository;

import com.example.jobconnect.model.JobApplication;
import com.example.jobconnect.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJob(Job job);
}
