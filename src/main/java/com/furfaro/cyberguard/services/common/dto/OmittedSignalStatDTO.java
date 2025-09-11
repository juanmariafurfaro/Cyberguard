package com.furfaro.cyberguard.services.common.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class OmittedSignalStatDTO {
    Long signalId;
    long omisiones;  // cuántas veces faltó detectarse en evaluaciones
}