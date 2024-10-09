package com.example.demo.service;

import com.example.demo.model.ResumeSection;
import com.example.demo.model.ResumeSuggestion;
import com.example.demo.repository.ResumeSuggestionRepository;
import dev.langchain4j.model.openai.OpenAiChatModel;
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
    private ResumeAnalysisService resumeAnalysisService;

    @Autowired
    public ResumeSuggestionService(PdfParseService pdfParseService) {
        this.pdfParseService = pdfParseService;
        this.sectionExtractor = new ResumeSectionExtractor();
        this.resumeAnalysisService = new ResumeAnalysisService(OpenAiChatModel.builder()
                .apiKey("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")  // Use your actual OpenAI API key here
                .build());
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

    public String generateParsedResume(byte[] fileData) throws IOException {
        String resumeText = pdfParseService.parsePdf(fileData);

        // Step 2: Extract relevant sections
        String educationSection = sectionExtractor.extractEducationSection(resumeText);
        String workExperienceSection = sectionExtractor.extractWorkExperienceSection(resumeText);
        String projectSection = sectionExtractor.extractProjectSection(resumeText);
        return educationSection + "\n\n"
                + workExperienceSection + "\n\n"
                +  projectSection + "\n\n";
    }

    public ResumeSection generateAIParsedResume(byte[] fileData) throws IOException {
        String resumeText = pdfParseService.parsePdf(fileData);

        // Step 2: Extract relevant sections
        String educationSection = resumeAnalysisService.extractEducationSection(resumeText);
        String workExperienceSection = resumeAnalysisService.extractWorkSection(resumeText);
        String projectSection = resumeAnalysisService.extractProjectSection(resumeText);
        String skillSection = resumeAnalysisService.extractSkillSection(resumeText);
        return new ResumeSection(educationSection, workExperienceSection, projectSection, skillSection);
    }

    public String generateSuggestion(byte[] fileData, String jobDescription) throws IOException {
        // Step 1: Parse the resume PDF to extract text
        String resumeText = pdfParseService.parsePdf(fileData);

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
        return educationSection + "\n\n"
                + workExperienceSection + "\n\n"
                +  projectSection + "\n\n";
    }
}
