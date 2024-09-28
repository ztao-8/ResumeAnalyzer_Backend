package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fileName;

    @Lob
    private byte[] data;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
