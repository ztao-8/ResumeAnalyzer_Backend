package com.example.demo.controller;

import com.example.demo.model.ResumeSuggestion;
import com.example.demo.service.ResumeSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/resume-suggestions")
public class ResumeSuggestionController {
    @Autowired
    private ResumeSuggestionService resumeSuggestionService;

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

}
