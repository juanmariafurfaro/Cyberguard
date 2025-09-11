package com.furfaro.cyberguard.services.run.dto;

import com.furfaro.cyberguard.models.run.InteractionType;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class InteractionDTO {
    InteractionType tipo; // HOVER | CLICK | SELECCION | TEXTO
    String payloadJson;   // detalle (ej: {elementId: "...", value: "..."} )
}