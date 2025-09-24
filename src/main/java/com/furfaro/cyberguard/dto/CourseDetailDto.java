package com.furfaro.cyberguard.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CourseDetailDto {
    private Long id;
    private String title;
    private String description;
    private String courseType;
    private Integer passingScore;
    private List<ModuleDto> modules;
    private Double overallProgress;
    private Boolean isCompleted;
    private Integer totalScore;
    private Integer maxTotalScore;
}