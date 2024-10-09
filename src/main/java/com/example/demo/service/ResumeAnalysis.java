package com.example.demo.service;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class ResumeAnalysis {
    private final OpenAiChatModel model;

    public ResumeAnalysis(OpenAiChatModel model) {
        this.model = model;
    }

    public String ResumeAnalysisWithJob(String resumeText, String jobDescription) {
        ResumeComparisonExample.ComparisonPrompt createPrompt = new ResumeComparisonExample.ComparisonPrompt(resumeText,jobDescription);
        Prompt prompt = StructuredPromptProcessor.toPrompt(createPrompt);
        return model.generate(prompt.text());
    }
}
