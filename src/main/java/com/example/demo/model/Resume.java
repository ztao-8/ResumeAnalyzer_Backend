package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String filePath;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
