package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    // Map this field to the 'fileName' column
    private String fileName;

    // Map this field to the 'fileType' column
    private String fileType;

    @Lob
//    @Column(name = "fileData")
    @Column(columnDefinition = "longblob")// Map this field to the 'fileData' column
    private byte[] fileData;  // Store the PDF as binary data


    public Resume(Long userId, String fileName, String fileType, byte[] fileData) {
        this.userId = userId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }

    // Default constructor (needed by JPA)
    public Resume() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public byte[] getFileData() {
        return fileData;
    }
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileName() {
        return fileName;
    }
}
