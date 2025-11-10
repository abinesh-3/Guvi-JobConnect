package com.example.jobconnect.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length=4000)
    private String description;

    private String location;

    private String salary;

    private LocalDate deadline;

    @ManyToOne
    private User employer;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public User getEmployer() { return employer; }
    public void setEmployer(User employer) { this.employer = employer; }
}
