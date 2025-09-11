package com.furfaro.cyberguard.services.signal.dto;

import com.furfaro.cyberguard.models.signal.SignalCategory;
import lombok.Data;

@Data
public class SignalDTO {
    private Long id;                 // null = crear
    private SignalCategory categoria;
    private String descripcion;
}