package com.furfaro.cyberguard.services.run.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class EvaluationDTO {
    Long attemptId;
    int puntaje;      // suma de pesos acertados
    int maxPuntaje;   // suma de pesos esperados
    int aciertos;     // #señales detectadas que eran esperadas
    int omisiones;    // #señales esperadas no detectadas
    String feedback;  // feedback formativo (texto)
}