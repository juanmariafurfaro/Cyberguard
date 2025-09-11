package com.furfaro.cyberguard.services.admin.dto;

import lombok.Data;

@Data
public class ScenarioUpsertDTO {
    private Long id;            // null = crear
    private Long simulacionId;
    private String nombre;
    private String descripcion;
    private Integer orden;
    private Integer dificultad;  // 1..5
    private Boolean active;
}