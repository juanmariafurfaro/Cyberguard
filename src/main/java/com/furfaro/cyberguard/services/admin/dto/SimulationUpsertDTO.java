package com.furfaro.cyberguard.services.admin.dto;

import com.furfaro.cyberguard.models.sim.SimulationType;
import lombok.Data;

@Data
public class SimulationUpsertDTO {
    private Long id;            // null = crear
    private Long moduloId;      // debe ser de tipo SIMULACION
    private String nombre;
    private String descripcion;
    private SimulationType tipo; // PHISHING | PRETEXTING | ...
    private Integer orden;
    private Boolean active;
}