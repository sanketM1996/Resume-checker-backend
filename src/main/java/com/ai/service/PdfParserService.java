package com.ai.service;

import com.ai.exception.InvalidFileFormatException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;



@Service
public class PdfParserService {

    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) throws TikaException, IOException {
        validatePdf(file);
        return tika.parseToString(file.getInputStream());
    }

    private void validatePdf(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (file.isEmpty()) {
            throw new InvalidFileFormatException("Uploaded file is empty.");
        }

        if (!"application/pdf".equalsIgnoreCase(contentType) ||
                (filename != null && !filename.toLowerCase().endsWith(".pdf"))) {
            throw new InvalidFileFormatException("Only PDF files are allowed.");
        }
    }
}

