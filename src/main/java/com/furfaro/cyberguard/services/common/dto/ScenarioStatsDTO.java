package com.furfaro.cyberguard.services.common.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ScenarioStatsDTO {
    Long scenarioId;
    long attempts;         // intentos totales
    long completed;        // finalizados
    double completionRate; // completed / attempts
    double avgDurationSec; // promedio de (fin - inicio) en segundos
}