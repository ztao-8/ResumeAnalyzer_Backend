package com.example.demo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.security.PublicKey;

public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String company;
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
