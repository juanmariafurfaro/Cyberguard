package com.furfaro.cyberguard.services.quiz.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class QuizResultDTO {
    Long questionId;
    Long selectedOptionId;
    boolean correcta;
}
