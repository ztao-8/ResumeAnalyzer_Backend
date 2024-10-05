package com.example.demo.service;
import com.example.demo.model.Job;
import com.example.demo.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    // Save a job to the database
    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    // Get a job by ID
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }
}
