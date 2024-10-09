package com.example.demo.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeSectionExtractor {
    private String extractSection(String resumeText, String sectionHeaderPattern, String nextSectionHeaderPattern) {

        String finalPattern = "(?i)(" + sectionHeaderPattern + ")([^\n]*\n+)(.*?)(?=(\\n\\s*\\n|(?i)" + nextSectionHeaderPattern + "|$))";
        Pattern pattern = Pattern.compile(finalPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(resumeText);

        if (matcher.find()) {
            String sectionHeader = matcher.group(1).trim();
            String sameLineContent = matcher.group(2).trim();
            String sectionContent = matcher.group(3).trim();
            return sectionHeader + ":\n" + sameLineContent + "\n" + sectionContent;
        }
        return "No section found for " + sectionHeaderPattern;
    }

    // Method to extract the Education Section
    public String extractEducationSection(String resumeText) {
        // Education-related section headers
        String educationHeaders = "education|academic background|qualifications|academic qualifications|academic";
        // Stop extraction when encountering work experience, projects, or other irrelevant sections
        String stopSections = "work experience|professional experience|projects|skills|certifications|languages|TECHNOLOGY|Technical";
        return extractSection(resumeText, educationHeaders, stopSections);
    }

    // Method to extract the Work Experience Section
    public String extractWorkExperienceSection(String resumeText) {
        // Work experience-related section headers
        String workExperienceHeaders = "EXPERIENCE|work experience|professional experience|employment history|career history|employment";
        // Stop extraction when encountering other sections like education, projects, etc.
        String stopSections = "education|projects|skills|certifications|languages";
        return extractSection(resumeText, workExperienceHeaders, stopSections);
    }

    // Method to extract the Projects Section
    public String extractProjectSection(String resumeText) {
        // Project-related section headers
        String projectHeaders = "projects|key projects|technical projects|significant projects";
        // Stop extraction when encountering other sections like education, work experience, etc.
        String stopSections = "work experience|skills|certifications|education|languages";
        return extractSection(resumeText, projectHeaders, stopSections);
    }
}
