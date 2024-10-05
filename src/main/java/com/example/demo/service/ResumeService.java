package com.example.demo.service;

import com.example.demo.model.Resume;
import com.example.demo.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private static final String UPLOAD_DIR = "/uploads/resumes/";

    @Autowired
    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume saveResume(MultipartFile file, Long userId) throws IOException {
        String fileName = userId + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        Optional<Resume> existingResume = resumeRepository.findByUserId(userId);
        if (existingResume.isPresent() && existingResume.get().getFilePath().equals(filePath)) {
            // Generate a new file name by appending a UUID to avoid conflict
            String uniqueFileName = userId + "_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            filePath = UPLOAD_DIR + uniqueFileName;
        }

        File localFile = new File(filePath);
        file.transferTo(localFile);
        Resume resume = new Resume(userId, filePath);
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
            String fileName = userId + "_" + file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;

            File localFile  = new File(filePath);
            file.transferTo(localFile);
            resume.setFilePath(filePath);
            return Optional.of(resumeRepository.save(resume));
        }
        return Optional.empty();
    }
}
