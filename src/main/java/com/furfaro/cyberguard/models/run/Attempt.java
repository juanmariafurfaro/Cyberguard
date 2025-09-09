package com.furfaro.cyberguard.models.run;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.sim.Scenario;
import com.furfaro.cyberguard.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "intentos", indexes = {
        @Index(name="ix_intento_usuario", columnList = "usuario_id"),
        @Index(name="ix_intento_escenario", columnList = "escenario_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attempt extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "escenario_id")
    private Scenario escenario;

    @Column(nullable = false) private Instant inicio;
    private Instant fin;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AttemptState estado = AttemptState.EN_PROGRESO;
}