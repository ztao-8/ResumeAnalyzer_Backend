package com.example.demo.service;

import com.example.demo.model.ResumeSuggestion;
import com.example.demo.repository.ResumeSuggestionRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ResumeSuggestionService {
    @Autowired
    private ResumeSuggestionRepository resumeSuggestionRepository;

    private PdfParseService pdfParseService;
    private ResumeSectionExtractor sectionExtractor;

    @Autowired
    public ResumeSuggestionService(PdfParseService pdfParseService) {
        this.pdfParseService = pdfParseService;
        this.sectionExtractor = new ResumeSectionExtractor();
    }


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

    public String generateSuggestion(MultipartFile file, String jobDescription) throws IOException {
        // Step 1: Parse the resume PDF to extract text
        String resumeText = pdfParseService.parsePdf(file);

        // Step 2: Extract relevant sections
        String educationSection = sectionExtractor.extractEducationSection(resumeText);
        String workExperienceSection = sectionExtractor.extractWorkExperienceSection(resumeText);
        String projectSection = sectionExtractor.extractProjectSection(resumeText);

        // Step 3: Summarize sections and analyze the full resume using LangChain4j
//        String educationSummary = resumeAnalysisService.summarizeSection("Education", educationSection);
//        String workExperienceSummary = resumeAnalysisService.summarizeSection("Work Experience", workExperienceSection);
//        String projectSummary = resumeAnalysisService.summarizeSection("Projects", projectSection);
//
//        String strengthsAndWeaknesses = resumeAnalysisService.analyzeStrengthsAndWeaknesses(resumeText);

        // Return all suggestions and summaries
        return "Education Summary:\n" + educationSection + "\n\n"
                + "Work Experience Summary:\n" +workExperienceSection + "\n\n"
                + "Project Summary:\n" + projectSection + "\n\n";
    }
}
