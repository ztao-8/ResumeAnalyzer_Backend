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
        String fileName = userId + "_" + file.getOriginalFilename();
        File uploadPath = new File(uploadDir);

        // Create the directory if it doesn't exist
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String filePath = uploadDir + File.separator + fileName;

        // Check if a resume already exists with the same path
        Optional<Resume> existingResume = resumeRepository.findByUserId(userId);
        if (existingResume.isPresent() && existingResume.get().getFilePath().equals(filePath)) {
            // Generate a new file name by appending a UUID to avoid conflict
            String uniqueFileName = userId + "_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            filePath = uploadDir + File.separator + uniqueFileName;
        }

        File localFile = new File(filePath);
        file.transferTo(localFile);  // Save the file on the file system
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
            String filePath = uploadDir + File.separator + fileName;

            File localFile  = new File(filePath);
            file.transferTo(localFile);  // Overwrite the existing file
            resume.setFilePath(filePath);
            return Optional.of(resumeRepository.save(resume));
        }
        return Optional.empty();
    }
}
