package com.furfaro.cyberguard.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationMessageDto {
    private String sender;
    private String message;
    private String timestamp;
    private Boolean isFromTarget; // true si es del perfil objetivo, false si es del atacante
}
