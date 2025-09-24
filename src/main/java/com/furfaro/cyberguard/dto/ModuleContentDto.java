package com.furfaro.cyberguard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleContentDto {
    private Long id;
    private String title;
    private String contentType;
    private String textContent;
    private String mediaUrl;
    private String mediaDescription;
    private Integer orderIndex;
    private String metadata;
}