package com.furfaro.cyberguard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitAnswerResponse {
    private Boolean isCorrect;
    private Integer pointsEarned;
    private Integer totalPoints;
    private String explanation;
    private String correctAnswer;
    private Boolean moduleCompleted;
    private Double moduleProgress;
}