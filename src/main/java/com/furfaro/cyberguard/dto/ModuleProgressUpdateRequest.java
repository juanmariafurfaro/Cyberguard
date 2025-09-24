package com.furfaro.cyberguard.dto;


import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
public class ModuleProgressUpdateRequest {
    
    @NotNull(message = "Module ID is required")
    private Long moduleId;
    
    @Min(value = 0, message = "Completion percentage must be between 0 and 100")
    @Max(value = 100, message = "Completion percentage must be between 0 and 100")
    private Double completionPercentage;
    
    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED
}