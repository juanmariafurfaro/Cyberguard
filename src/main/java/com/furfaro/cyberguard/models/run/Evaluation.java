package com.furfaro.cyberguard.models.run;


import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "evaluaciones",
        uniqueConstraints = @UniqueConstraint(name = "uk_eval_intento", columnNames = "intento_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evaluation extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "intento_id")
    private Attempt intento;

    @Column(nullable = false) private int puntaje;
    @Column(nullable = false) private int aciertos;
    @Column(nullable = false) private int omisiones;

    @Column(columnDefinition = "text") private String feedback;
    @Column(nullable = false) private Instant fecha;
}
