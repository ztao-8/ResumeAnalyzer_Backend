package com.example.demo.service;

import com.example.demo.model.Resume;
import com.example.demo.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private static final String UPLOAD_DIR = "/uploads/resumes/";

    @Autowired
    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume saveResume(MultipartFile file, Long userId) throws IOException {
        Resume resume = new Resume();

        String fileName = userId + "_" + file.getOriginalFilename();
        String filePath = UPLOAD_DIR + fileName;

        File localFile  = new File(filePath);
        file.transferTo(localFile);

        resume.setUserId(userId);
        resume.setFilePath(filePath);
        resume.setFileName(file.getOriginalFilename());
        // Save the resume in the database
        return resumeRepository.save(resume);
    }

    public Optional<Resume> getById(Long id) {
        return resumeRepository.findById(id);
    }
}
