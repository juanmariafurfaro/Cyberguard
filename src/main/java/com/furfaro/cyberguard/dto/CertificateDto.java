package com.furfaro.cyberguard.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CertificateDto {
    private Long id;
    private String courseTitle;
    private String certificateCode;
    private LocalDateTime issuedDate;
    private Double finalScore;
}