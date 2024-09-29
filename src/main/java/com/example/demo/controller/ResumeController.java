package com.example.demo.controller;

import com.example.demo.model.Resume;
import com.example.demo.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) throws IOException {
        Resume uploadedResume = resumeService.saveResume(file, userId);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<?> getResume(@PathVariable("id") Long id) {
        Optional<Resume> resume = resumeService.getById(id);
        if (resume.isPresent()) {
            Resume resumeUploaded = resume.get();
            try {
//                Path filePath = Paths.get(resumeUploaded.getFilePath());
                File file = new File(resumeUploaded.getFilePath());

                FileSystemResource resource = new FileSystemResource(file);
                String contentType = Files.probeContentType(file.toPath());

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } catch (Exception e) {
                throw new RuntimeException("Error while serving the file", e);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
