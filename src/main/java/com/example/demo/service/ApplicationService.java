package com.example.demo.service;

import com.example.demo.model.Application;
import com.example.demo.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    // Save an application
    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    public List<Application> getAllByUserId(Long userId) {
        return applicationRepository.findByUserId(userId);

    }

    public Optional<Application> updateApplicationStatus(Long userId, Long jobId, String status) {
        Optional<Application> applicationOpt = applicationRepository.findByUserIdAndJobId(userId, jobId);

        if (applicationOpt.isPresent()) {
            Application application = applicationOpt.get();
            application.setStatus(status);  // Update the status
            applicationRepository.save(application);  // Save the updated application
            return Optional.of(application);  // Return the updated application
        }

        return Optional.empty();  // If application doesn't exist, return empty
    }


}
