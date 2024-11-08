package com.example.demo.service;

import com.example.demo.model.Application;
import com.example.demo.model.Job;
import com.example.demo.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private JobService jobService;

    // Save an application
    public Application saveApplication(Map<String, Object> applicationJobData) {
        String jobTitle = (String) applicationJobData.get("title");
        String company = (String) applicationJobData.get("company");
        String jobUrl = (String) applicationJobData.get("url");

        Job job = new Job(jobTitle, company, jobUrl);
        jobService.addJob(job);

        Long userId = ((Number) applicationJobData.get("userId")).longValue();
        String status = (String) applicationJobData.get("status");
        Long jobId = job.getId();

        Application application = new Application(jobId, userId, status);
        return applicationRepository.save(application);
    }

    public List<Map<String, String>> getAllByUserId(Long userId) {
        List<Application> applications = applicationRepository.findByUserId(userId);
        if (applications.isEmpty()) {
            return List.of(); // Return an empty list if no applications are found
        }
        List<Map<String, String>> result = new ArrayList<>();
        for (Application application : applications) {
            Map<String, String> applicationInfo = new HashMap<>();
            jobService.getJobById(application.getJobId()).ifPresentOrElse(
                    job -> {
                        applicationInfo.put("jobID", job.getId().toString());
                        applicationInfo.put("title", job.getTitle());
                        applicationInfo.put("company", job.getCompany());
                    },
                    () -> {
                        // Log a message or handle missing job information
                        System.err.println("Job not found for job ID: " + application.getJobId());
                        applicationInfo.put("title", "Unknown");
                        applicationInfo.put("company", "Unknown");
                    }
            );
            applicationInfo.put("status", application.getStatus());
            result.add(applicationInfo);
        }
        return result;
    }

    public Optional<Application> updateApplicationStatus(Long userId, Long jobId, String status) {
        Optional<Application> applicationOpt = applicationRepository.findByUserIdAndJobId(userId, jobId);

        if (applicationOpt.isPresent()) {
            Application application = applicationOpt.get();
            application.setStatus(status);  // Update the status
            applicationRepository.save(application);  // Save the updated application
            return Optional.of(application);  // Return the updated application
        }

        return Optional.empty();
    }


}
