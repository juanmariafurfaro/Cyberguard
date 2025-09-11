package com.furfaro.cyberguard.services.admin.dto;

import com.furfaro.cyberguard.models.course.ModuleType;
import lombok.Data;

@Data
public class ModuleUpsertDTO {
    private Long id;            // null = crear
    private Long cursoId;
    private String nombre;
    private ModuleType tipo;    // TEORIA | SIMULACION | QUIZ
    private Integer orden;      // requerido
    private Boolean active;     // opcional
}