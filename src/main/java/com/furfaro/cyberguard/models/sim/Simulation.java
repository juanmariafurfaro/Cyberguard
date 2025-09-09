package com.furfaro.cyberguard.models.sim;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.course.Module;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "simulaciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Simulation extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modulo_id")
    private Module modulo; // debe ser de tipo SIMULACION

    @Column(nullable = false, length = 140) private String nombre;
    @Column(columnDefinition = "text") private String descripcion;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SimulationType tipo;
    @Column(nullable = false) private int orden;
    @Column(nullable = false) private boolean isActive = true;
}