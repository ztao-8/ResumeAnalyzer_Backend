package com.example.demo.service;

import com.example.demo.model.ResumeSuggestion;
import com.example.demo.repository.ResumeSuggestionRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResumeSuggestionService {
    @Autowired
    private ResumeSuggestionRepository resumeSuggestionRepository;

    public ResumeSuggestion saveSuggestion(ResumeSuggestion resumeSuggestion) {
        return resumeSuggestionRepository.save(resumeSuggestion);
    }

    // Update an existing suggestion by resumeId
    public Optional<ResumeSuggestion> updateSuggestion(Long resumeId, String newSuggestion, Integer newScore) {
        Optional<ResumeSuggestion> suggestionOpt = resumeSuggestionRepository.findByResumeId(resumeId);

        if (suggestionOpt.isPresent()) {
            ResumeSuggestion existingSuggestion = suggestionOpt.get();
            existingSuggestion.setSuggestion(newSuggestion);   // Update suggestion
            existingSuggestion.setScore(newScore);             // Update score
            return Optional.of(resumeSuggestionRepository.save(existingSuggestion)); // Save and return updated suggestion
        }

        return Optional.empty();  // If not found, return empty
    }

    public Optional<ResumeSuggestion> getSuggestionByResumeId(Long resumeId) {
        return resumeSuggestionRepository.findByResumeId(resumeId);
    }
}
