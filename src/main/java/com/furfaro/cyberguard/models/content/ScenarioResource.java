package com.furfaro.cyberguard.models.content;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.sim.Scenario;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "escenario_recurso",
        uniqueConstraints = @UniqueConstraint(name="uk_escenario_recurso", columnNames = {"escenario_id","recurso_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ScenarioResource extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "escenario_id")
    private Scenario escenario;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "recurso_id")
    private Resource recurso;
}