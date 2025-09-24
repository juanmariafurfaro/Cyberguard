package com.furfaro.cyberguard.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PhishingEmailDto {
    private Long questionId;
    private String senderEmail;
    private String emailSubject;
    private String emailContent;
    private List<String> options; // "Es phishing", "Es legítimo"
    private String explanation;
}