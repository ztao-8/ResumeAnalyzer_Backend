package com.example.demo.controller;

import com.example.demo.model.Resume;
import com.example.demo.model.ResumeSection;
import com.example.demo.model.ResumeSuggestion;
import com.example.demo.service.ResumeService;
import com.example.demo.service.ResumeSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/resume-suggestions")
public class ResumeSuggestionController {
    @Autowired
    private ResumeSuggestionService resumeSuggestionService;
    @Autowired
    private ResumeService resumeService;

    @PostMapping("/add")
    public ResponseEntity<ResumeSuggestion> saveSuggestion(@RequestBody ResumeSuggestion resumeSuggestion) {
        ResumeSuggestion savedSuggestion = resumeSuggestionService.saveSuggestion(resumeSuggestion);
        return ResponseEntity.ok(savedSuggestion);  // Return saved suggestion
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeSuggestion> getSuggestionByResumeId(@PathVariable Long resumeId) {
        Optional<ResumeSuggestion> suggestion = resumeSuggestionService.getSuggestionByResumeId(resumeId);

        return suggestion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{resumeId}")
    public ResponseEntity<ResumeSuggestion> updateSuggestion(
            @PathVariable Long resumeId,
            @RequestParam String suggestion,
            @RequestParam Integer score) {

        Optional<ResumeSuggestion> updatedSuggestion = resumeSuggestionService.updateSuggestion(resumeId, suggestion, score);

        return updatedSuggestion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/parse/{userId}")
    public ResponseEntity<String> parseResume(@PathVariable Long userId) {
        Optional<Resume> resumeOpt = resumeService.getByUserId(userId);
        if (resumeOpt.isPresent()) {
            Resume resume = resumeOpt.get();
            byte[] fileData = resume.getFileData();
            // Generate and store suggestions
            try {
                String parsedResume = resumeSuggestionService.generateParsedResume(fileData);
                return ResponseEntity.ok("Parse complete. Resume: " + parsedResume);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error analyzing resume");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume not found");
        }
    }

    @PostMapping("/parseAI/{userId}")
    public ResponseEntity<?> parseAIResume(@PathVariable Long userId) {
        Optional<Resume> resumeOpt = resumeService.getByUserId(userId);
        if (resumeOpt.isPresent()) {
            Resume resume = resumeOpt.get();
            // Directly use the fileData byte array
            byte[] fileData = resume.getFileData();
            // Generate and store suggestions
            try {
                ResumeSection parsedResume = resumeSuggestionService.generateAIParsedResume(fileData);
                return ResponseEntity.ok(parsedResume);
            } catch (IOException e) {
                // Handle the IOException by returning an INTERNAL_SERVER_ERROR response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while parsing the resume.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume not found");
        }
    }

    @PostMapping("/analysis/{userId}")
    //input contain jobDescription
    public ResponseEntity<?> analyzeAIResume(@PathVariable Long userId, @RequestParam String jobDescription) {
        Optional<Resume> resumeOpt = resumeService.getByUserId(userId);
        if (resumeOpt.isPresent()) {
            Resume resume = resumeOpt.get();
            byte[] fileData = resume.getFileData();
            try {
                String suggestion = resumeSuggestionService.generateSuggestion(fileData,jobDescription);
                return ResponseEntity.ok(suggestion);
            } catch (IOException e) {
                // Handle the IOException by returning an INTERNAL_SERVER_ERROR response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while analzing the resume.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume not found");
        }
    }

}
