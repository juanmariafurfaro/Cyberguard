package com.furfaro.cyberguard.models.sim;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "escenarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Scenario extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "simulacion_id")
    private Simulation simulacion;

    @Column(nullable = false, length = 160) private String nombre;
    @Column(columnDefinition = "text") private String descripcion;
    @Column(nullable = false) private int orden;
    @Column(nullable = false) private int dificultad; // 1..5
    @Column(nullable = false) private boolean isActive = true;
}