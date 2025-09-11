package com.furfaro.cyberguard.services.sim.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value @Builder
public class ScenarioViewDTO {
    Long id;
    Long simulacionId;
    String nombre;
    String descripcion;
    int orden;
    int dificultad;

    @Singular("recurso")
    List<ResourceViewDTO> recursos; // solo contenido para renderizar
}