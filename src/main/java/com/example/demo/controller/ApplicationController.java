package com.example.demo.controller;

import com.example.demo.model.Application;
import com.example.demo.model.Job;
import com.example.demo.repository.JobRepository;
import com.example.demo.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/add")
    public ResponseEntity<Application> addApplication(@RequestBody Map<String, Object> applicationJobData) {
        Application savedApplication = applicationService.saveApplication(applicationJobData);
        return ResponseEntity.ok(savedApplication);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<Map<String,String>>>  getApplication(@PathVariable Long userId) {
        List<Map<String,String>> userApplication = applicationService.getAllByUserId(userId);
        return ResponseEntity.ok(userApplication);
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Application>>  getApplication(@PathVariable Long userId) {
//        List<Application> userApplication = applicationService.getAllByUserId(userId);
//        return ResponseEntity.ok(userApplication);
//    }

    @PutMapping("/user/{userId}/job/{jobId}/status")
    public ResponseEntity<Application> updateApplication(@PathVariable Long userId, @PathVariable Long jobId, @RequestParam String status) {
        Optional<Application> application = applicationService.updateApplicationStatus(userId,jobId, status);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
