package com.furfaro.cyberguard.services.run.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class UpsertSignalResponseDTO {
    Long signalId;
    boolean detectada;
    String comentario;
}