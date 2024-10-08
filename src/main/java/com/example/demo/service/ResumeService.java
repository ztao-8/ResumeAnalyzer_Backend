package com.example.demo.service;

import com.example.demo.model.Resume;
import com.example.demo.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "resumes";
    @Autowired
    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume saveResume(MultipartFile file, Long userId) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        byte[] fileData = file.getBytes();
        Resume resume = new Resume(userId,fileName,fileType,fileData);
        // Save the resume in the database
        return resumeRepository.save(resume);
    }

    public Optional<Resume> getByUserId(Long userId) {
        return resumeRepository.findByUserId(userId);
    }

    public Optional<Resume> updateResume(Long userId, MultipartFile file) throws IOException {
        Optional<Resume> existingResume = resumeRepository.findByUserId(userId);
        if (existingResume.isPresent()) {
            Resume resume = existingResume.get();
            byte[] newFileData = file.getBytes();
            resume.setFileData(newFileData);
            return Optional.of(resumeRepository.save(resume));
        }
        return Optional.empty();
    }
}
