package com.ai.controller;

import com.ai.dto.ResumeAnalysisResponse;
import com.ai.service.PdfParserService;
import com.ai.service.ResumeAnalyzerService;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private PdfParserService pdfParserService;

    @Autowired
    private ResumeAnalyzerService resumeAnalyzerService;

    @PostMapping("/upload")
    public ResponseEntity<ResumeAnalysisResponse> uploadResume(@RequestParam("file") MultipartFile file)
            throws IOException, TikaException {
        String resumeText = pdfParserService.extractText(file);
        ResumeAnalysisResponse response = resumeAnalyzerService.analyzeResumeText(resumeText);
        return ResponseEntity.ok(response);
    }

}
