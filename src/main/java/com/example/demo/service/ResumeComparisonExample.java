package com.example.demo.service;
import dev.langchain4j.model.input.structured.StructuredPrompt;

public class ResumeComparisonExample {
    @StructuredPrompt({
            "Compare the resume with the job description to identify the candidate's strengths and weaknesses.",
            "Provide the following information:",
            "",
            "Strengths:",
            "- ...",
            "",
            "Weaknesses:",
            "- ...",
            "",
            "Suitability score (0-100): ..."
    })
    static class ComparisonPrompt {

        private String resume;
        private String jobDescription;

        ComparisonPrompt(String resume, String jobDescription) {
            this.resume = resume;
            this.jobDescription = jobDescription;
        }
    }
}
