package com.furfaro.cyberguard.services.sim.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ScenarioDTO {
    Long id;
    Long simulacionId;
    String nombre;
    String descripcion;
    int orden;
    int dificultad;
    boolean active;
}