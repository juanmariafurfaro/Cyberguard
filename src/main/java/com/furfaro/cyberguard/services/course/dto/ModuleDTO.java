package com.furfaro.cyberguard.services.course.dto;

import com.furfaro.cyberguard.models.course.ModuleType;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ModuleDTO {
    Long id;
    Long cursoId;
    String nombre;
    ModuleType tipo; // TEORIA | SIMULACION | QUIZ
    int orden;
    boolean active;
}