package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ResumeSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long resumeId;
    private String suggestion;
    private Integer score;

    public ResumeSuggestion() {}

    public ResumeSuggestion(Long resumeId, String suggestion, Integer score) {
        this.resumeId = resumeId;
        this.suggestion = suggestion;
        this.score = score;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public Long getId() {
        return id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public Integer getScore() {
        return score;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
