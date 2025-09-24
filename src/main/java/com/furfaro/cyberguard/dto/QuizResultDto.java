package com.furfaro.cyberguard.dto;


import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class QuizResultDto {
    private Long moduleId;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer totalPoints;
    private Integer maxPoints;
    private Double percentage;
    private Boolean passed;
    private String feedback;
    private List<QuestionResultDto> questionResults;
}