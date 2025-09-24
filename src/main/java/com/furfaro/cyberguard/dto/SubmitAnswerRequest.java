package com.furfaro.cyberguard.dto;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class SubmitAnswerRequest {
    
    @NotNull(message = "Question ID is required")
    private Long questionId;
    
    private Long selectedOptionId; // Para preguntas de opción múltiple
    
    private String textAnswer; // Para respuestas de texto libre
    
    private Integer attemptNumber;
}