package com.furfaro.cyberguard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseProgressDto {
    private Long courseId;
    private String courseTitle;
    private String courseDescription;
    private Integer currentModule;
    private Double completionPercentage;
    private Boolean isCompleted;
    private Double finalScore;
}