package com.furfaro.cyberguard.services.badge.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value @Builder
public class AchievementDTO {
    Long id;
    String nombre;
    String descripcion;
    Instant fechaObtencion; // null si es del catálogo (no del usuario)
}