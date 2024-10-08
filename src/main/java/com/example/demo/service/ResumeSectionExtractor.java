package com.example.demo.service;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class ResumeSectionExtractor {
//
//    // Method to extract Education Section
//    public String extractEducationSection(String resumeText) {
//        // Regular expressions to match various patterns for education sections
//        String educationPattern = "(?i)(education|academic background|qualifications|academic qualifications|academic).*?\\n(.*?)(?=\\n\\s*\\n|$)";
//        Pattern pattern = Pattern.compile(educationPattern, Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(resumeText);
//
//        if (matcher.find()) {
//            return matcher.group(2).trim();
//        }
//        return "No education section found.";
//    }
//
//    // Method to extract Work Experience Section
//    public String extractWorkExperienceSection(String resumeText) {
//        // Regular expressions to match various patterns for work experience sections
//        String experiencePattern = "(?i)(work experience|professional experience|employment history|career history|employment).*?\\n(.*?)(?=\\n\\s*\\n|$)";
//        Pattern pattern = Pattern.compile(experiencePattern, Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(resumeText);
//
//        if (matcher.find()) {
//            return matcher.group(2).trim();
//        }
//        return "No work experience section found.";
//    }
//
//    // Method to extract Project Section
//    public String extractProjectSection(String resumeText) {
//        // Regular expressions to match various patterns for project sections
//        String projectPattern = "(?i)(projects|key projects|technical projects).*?\\n(.*?)(?=\\n\\s*\\n|$)";
//        Pattern pattern = Pattern.compile(projectPattern, Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(resumeText);
//
//        if (matcher.find()) {
//            return matcher.group(2).trim();
//        }
//        return "No project section found.";
//    }
//}
//

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeSectionExtractor {

    // Common method to extract sections based on headers and next section delimiter
    private String extractSection(String resumeText, String sectionHeaderPattern, String nextSectionHeaderPattern) {
        // Construct the final regex pattern
        String finalPattern = "(?i)(" + sectionHeaderPattern + ").*?\\n(.*?)(?=(\\n\\s*\\n|(?i)" + nextSectionHeaderPattern + "|$))";
        Pattern pattern = Pattern.compile(finalPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(resumeText);

        if (matcher.find()) {
            return matcher.group(1).trim() + "\n" + matcher.group(2).trim();
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
