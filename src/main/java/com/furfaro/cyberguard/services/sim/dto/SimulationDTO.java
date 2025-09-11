package com.furfaro.cyberguard.services.sim.dto;

import com.furfaro.cyberguard.models.sim.SimulationType;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class SimulationDTO {
    Long id;
    Long moduloId;
    String nombre;
    String descripcion;
    SimulationType tipo;  // PHISHING | PRETEXTING | ...
    int orden;
    boolean active;
}