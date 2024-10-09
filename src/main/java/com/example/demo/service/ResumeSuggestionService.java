package com.example.demo.service;

import com.example.demo.model.ResumeSection;
import com.example.demo.model.ResumeSuggestion;
import com.example.demo.repository.ResumeSuggestionRepository;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Component
@Service
public class ResumeSuggestionService {
    @Autowired
    private ResumeSuggestionRepository resumeSuggestionRepository;
    private PdfParseService pdfParseService;
    private ResumeSectionExtractor sectionExtractor;
    private ResumeParser resumeParser;
    private ResumeAnalysis resumeAnalysis;
    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Autowired
    public ResumeSuggestionService(PdfParseService pdfParseService) {
        this.pdfParseService = pdfParseService;
        this.sectionExtractor = new ResumeSectionExtractor();
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .build();
        this.resumeParser = new ResumeParser(model);
        this.resumeAnalysis = new ResumeAnalysis(model);
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
        String educationSection = resumeParser.extractEducationSection(resumeText);
        String workExperienceSection = resumeParser.extractWorkSection(resumeText);
        String projectSection = resumeParser.extractProjectSection(resumeText);
        String skillSection = resumeParser.extractSkillSection(resumeText);
        return new ResumeSection(educationSection, workExperienceSection, projectSection, skillSection);
    }

    public String generateSuggestion(byte[] fileData, String jobDescription) throws IOException {
        String resumeText = pdfParseService.parsePdf(fileData);
        return resumeAnalysis.ResumeAnalysisWithJob(resumeText, jobDescription);
    }
}
