package com.example.demo.controller;

import com.example.demo.model.Resume;
import com.example.demo.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeController {

    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) throws IOException {
        Resume uploadedResume = resumeService.saveResume(file, userId);
        return ResponseEntity.ok("Resume uploaded with ID: " + uploadedResume.getId());
    }

    @GetMapping("/pdf/{userId}")
    public ResponseEntity<?> getResume(@PathVariable("userId") Long userId) {
        Optional<Resume> resume = resumeService.getByUserId(userId);
        if (resume.isPresent()) {
            Resume resumeUploaded = resume.get();
            try {
                // Get the binary PDF data from the fileData column
                byte[] pdfData = resumeUploaded.getFileData();

                // Return the PDF as a byte array
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(resumeUploaded.getFileType())) // e.g., "application/pdf"
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resumeUploaded.getFileName() + "\"")
                        .body(pdfData);  // Return binary data
            } catch (Exception e) {
                throw new RuntimeException("Error while serving the file", e);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateResume(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<Resume> updatedResume = resumeService.updateResume(userId, file);
        return updatedResume.isPresent() ? ResponseEntity.ok(updatedResume.get()) : ResponseEntity.notFound().build();
    }
}
