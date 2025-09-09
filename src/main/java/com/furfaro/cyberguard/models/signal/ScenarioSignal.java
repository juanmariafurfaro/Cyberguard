package com.furfaro.cyberguard.models.signal;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.sim.Scenario;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "senial_escenario",
        uniqueConstraints = @UniqueConstraint(name="uk_senial_escenario", columnNames = {"escenario_id","senial_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScenarioSignal extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "escenario_id")
    private Scenario escenario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "senial_id")
    private Signal senial;

    @Column(nullable = false) private int peso = 1;      // ponderación
    @Column(nullable = false) private boolean obligatoria = false;
}
