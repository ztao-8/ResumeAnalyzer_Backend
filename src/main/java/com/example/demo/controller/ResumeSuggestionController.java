package com.example.demo.controller;

import com.example.demo.model.Resume;
import com.example.demo.model.ResumeSuggestion;
import com.example.demo.service.ResumeService;
import com.example.demo.service.ResumeSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

//    @PostMapping("/analyze")
//    public ResponseEntity<?> analyzeResume(@RequestParam("file") MultipartFile file,
//                                           @RequestParam("jobDescription") String jobDescription) throws IOException {
//        // Generate resume suggestions and section summaries
//        String suggestions = resumeSuggestionService.generateSuggestion(file, jobDescription);
//
//        return ResponseEntity.ok(suggestions);
//    }
    @PostMapping("/analyze/{userId}")
    public ResponseEntity<String> analyzeResume(@PathVariable Long userId, @RequestParam("jobDescription") String jobDescription) {
        Optional<Resume> resumeOpt = resumeService.getByUserId(userId);
        if (resumeOpt.isPresent()) {
            Resume resume = resumeOpt.get();

            // Directly use the fileData byte array
            byte[] fileData = resume.getFileData();

            // Generate and store suggestions
            try {
                String suggestion = resumeSuggestionService.generateSuggestion(fileData, jobDescription);
                return ResponseEntity.ok("Analysis complete. Suggestions: " + suggestion);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error analyzing resume");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume not found");
        }
    }


}
