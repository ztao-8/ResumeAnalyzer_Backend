package com.example.demo.service;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class ResumeParser {

    private final OpenAiChatModel model;

    public ResumeParser(OpenAiChatModel model) {
        this.model = model;
    }

    public String extractEducationSection(String resumeText){
        return extractSection("Extract the education section", resumeText);
    }

    public String extractWorkSection(String resumeText){
        return extractSection("Extract the work experience section", resumeText);
    }

    public String extractSkillSection(String resumeText){
        return extractSection("Extract the skills section", resumeText);
    }

    public String extractProjectSection(String resumeText){
        return extractSection("Extract the projects section", resumeText);
    }

    private String extractSection(String instruction, String resumeText) {
        String promptText = instruction + ": " + resumeText;
        Prompt prompt = Prompt.from(promptText);
        return model.generate(prompt.text());
    }

}