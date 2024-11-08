package com.example.demo.controller;
import com.example.demo.model.Job;
//import com.example.demo.model.LinkedInJob;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // Add a job (stored in the database)
    @PostMapping("/add")
    public ResponseEntity<Job> addJob(@RequestBody Job job) {
        Job savedJob = jobService.addJob(job);
        return ResponseEntity.ok(savedJob);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        Optional<Job> job = jobService.getJobById(id);
        return job.isPresent() ? ResponseEntity.ok(job.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Map<String, String>> searchJobs(
            @RequestParam String keyword,
            @RequestParam String location
    ) {
        return jobService.searchJobs(keyword, location);
    }
}
