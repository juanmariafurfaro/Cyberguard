package com.furfaro.cyberguard.dto;


import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class QuestionDto {
    private Long id;
    private String questionText;
    private String questionType;
    private Integer orderIndex;
    private Integer points;
    private List<QuestionOptionDto> options;
    
    // Para phishing emails
    private String emailContent;
    private String senderEmail;
    private String emailSubject;
    
    // Para pretexting
    private String profileData;
    private String conversationData;
    
    private String explanation;
    private Boolean isAnswered;
    private Boolean isCorrect;
    private Integer userAnswer; // ID de la opción seleccionada por el usuario
}