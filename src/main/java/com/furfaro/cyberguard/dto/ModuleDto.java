package com.furfaro.cyberguard.dto;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ModuleDto {
    private Long id;
    private String title;
    private String description;
    private String moduleType;
    private Integer orderIndex;
    private Boolean isRequired;
    private Boolean isAccessible; // Si el usuario puede acceder a este módulo
    private Boolean isCompleted; // Si el usuario completó este módulo
    private Double completionPercentage;
    private Integer score;
    private Integer maxScore;
    private List<ModuleContentDto> contents;
    private List<QuestionDto> questions;
    private Long courseId;
}
