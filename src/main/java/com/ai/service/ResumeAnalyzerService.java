package com.ai.service;
import com.ai.dto.ResumeAnalysisResponse;


import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResumeAnalyzerService {

    private final ChatClient chatClient;

    public ResumeAnalyzerService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public ResumeAnalysisResponse analyzeResumeText(String resumeText) {
        String prompt = """
            Act as a professional resume reviewer.

            Analyze the following resume based on:
            - Experience and achievements
            - Technical skills (Java, Spring Boot, Microservices, Hibernate)
            - Resume formatting and structure
            - Clarity and grammar

            Provide the following in your response:
            1. Summary
            2. Strengths
            3. Weaknesses
            4. Suggestions
            5. Final Score: XX/100 (This must appear in the last line as: "Score: XX/100")

            Resume:
            %s
        """.formatted(resumeText);

        String aiResponse = chatClient.call(prompt);
        int score = extractScore(aiResponse);

        if (score == -1) {
            score = estimateFallbackScore(aiResponse);
            aiResponse += "\n\n(Estimated fallback score assigned: " + score + "/100)";
        }

        return new ResumeAnalysisResponse(score, aiResponse);
    }

    private int extractScore(String response) {
        Pattern pattern = Pattern.compile("Score:\\s*(\\d{1,3})\\s*/\\s*100", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            return Math.min(value, 100);
        }
        return -1;
    }

    private int estimateFallbackScore(String response) {
        if (response.toLowerCase().contains("excellent")) return 90;
        if (response.toLowerCase().contains("good")) return 80;
        if (response.toLowerCase().contains("average")) return 70;
        if (response.toLowerCase().contains("needs improvement")) return 60;
        return 75;
    }
}
