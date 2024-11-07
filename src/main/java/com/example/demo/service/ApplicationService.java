package com.example.demo.service;

import com.example.demo.model.Application;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
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
    public Application saveApplication(Application application) {
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
//    public List<Application> getAllByUserId(Long userId) {
//        return applicationRepository.findByUserId(userId);
//    }

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
