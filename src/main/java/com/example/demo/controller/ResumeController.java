package com.example.demo.controller;

import com.example.demo.model.Resume;
import com.example.demo.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        Resume uploadedResume = resumeService.saveResume(file, userId);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<?> getResume(@PathVariable("id") Long id) {
        Optional<Resume> resume = resumeService.getById(id);
        if (resume.isPresent()) {
            Resume resumeUploaded = resume.get();
            byte[] pdfData = resumeUploaded.getData(); // Assuming the PDF is stored as binary data (byte[])

            // Set content type and headers for the response
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + resumeUploaded.getFileName() + "\"") // This tells the browser to display it inline
                    .contentType(MediaType.APPLICATION_PDF) // Set MIME type as PDF
                    .body(pdfData); // Return the PDF binary content
        } else {
            return ResponseEntity.notFound().build(); // Handle case where resume is not found
        }
    }
}
