package com.furfaro.cyberguard.services.run.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class MissingSignalDTO {
    Long signalId;
    String categoria;   // ej: REMITENTE/ENLACE/URGENCIA
    String descripcion; // breve explicación a mostrar en feedback
    boolean obligatoria;
    int peso;
}