package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @Column(name = "job_title", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'Software Engineer Intern'")
    private String title;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "job_url", nullable = true)
    private String url;

    public Job(){
    }

    public Job(String title, String company, String url) {
        this.title = title;
        this.company = company;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
