package com.furfaro.cyberguard.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionOptionDto {
    private Long id;
    private String optionText;
    private Integer orderIndex;
    private Boolean isCorrect; // Solo se incluye en respuestas del servidor, no en requests
}