package com.ai.dto;

public class ResumeAnalysisResponse {
    private int score;
    private String feedback;

    public ResumeAnalysisResponse(int score, String feedback) {
        this.score = score;
        this.feedback = feedback;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }
}

