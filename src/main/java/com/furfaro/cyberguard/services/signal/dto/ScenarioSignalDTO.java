package com.furfaro.cyberguard.services.signal.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ScenarioSignalDTO {
    Long scenarioId;
    Long signalId;
    int peso;
    boolean obligatoria;
    String categoria;   // para mostrar
    String descripcion; // para mostrar
}