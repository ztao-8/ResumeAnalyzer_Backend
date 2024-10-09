package com.example.demo.model;

public class ResumeSection {
    private String education;
    private String workExperience;
    private String skills;
    private String projects;

    public ResumeSection(String education,String work,String skills, String projects) {
        this.education = education;
        this.workExperience = work;
        this.skills = skills;
        this.projects = projects;
    }
    // Getters and Setters

    public String getEducation() { return education; }
    public String getWorkExperience() { return workExperience; }
    public String getSkills() { return skills; }
    public String getProjects() { return projects; }

}

