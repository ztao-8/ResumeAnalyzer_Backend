package com.example.demo.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfParseService {

    public String parsePdf(String filePath) throws IOException {
        File pdfFile = new File(filePath);

        // Load PDF document
        PDDocument document = PDDocument.load(pdfFile);

        // Extract text from the document
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String parsedText = pdfStripper.getText(document);

        // Close the document
        document.close();

        return parsedText;
    }
}
