package com.furfaro.cyberguard.services.common.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class QuestionErrorStatDTO {
    Long questionId;
    long respuestas;   // total de respuestas
    long errores;      // cantidad incorrectas
    double wrongRate;  // errores / respuestas
}