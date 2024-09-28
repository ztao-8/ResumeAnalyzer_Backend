package com.example.demo.service;

import com.example.demo.model.Resume;
import com.example.demo.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    @Autowired
    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume saveResume(MultipartFile file, Long userId) {
        Resume resume = new Resume();
        try {
            resume.setUserId(userId);
            resume.setData(file.getBytes());
            resume.setFileName(file.getOriginalFilename());

            // Save the resume in the database
            return resumeRepository.save(resume);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save resume", e);
        }
    }
}
