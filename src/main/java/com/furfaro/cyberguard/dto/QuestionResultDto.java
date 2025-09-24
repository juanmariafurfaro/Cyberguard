package com.furfaro.cyberguard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResultDto {
    private Long questionId;
    private String questionText;
    private Boolean isCorrect;
    private Integer pointsEarned;
    private Integer maxPoints;
    private String userAnswer;
    private String correctAnswer;
    private String explanation;
}