package com.furfaro.cyberguard.models.run;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "interacciones", indexes = {
        @Index(name="ix_interaccion_intento", columnList = "intento_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Interaction extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "intento_id")
    private Attempt intento;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private InteractionType tipo;

    @Column(columnDefinition = "text") private String payloadJson; // detalle (posición, elemento, respuesta)
    @Column(nullable = false) private Instant timestamp;
}


