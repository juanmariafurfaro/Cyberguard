package com.furfaro.cyberguard.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PretextingConversationDto {
    private Long questionId;
    private ProfileDto profile;
    private List<ConversationMessageDto> conversation;
    private List<String> options; // "Es pretexting", "Es conversación normal"
    private String explanation;
}