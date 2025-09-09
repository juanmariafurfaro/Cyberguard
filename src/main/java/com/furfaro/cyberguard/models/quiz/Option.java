package com.furfaro.cyberguard.models.quiz;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "opciones",
        indexes = @Index(name="ix_opcion_pregunta", columnList = "pregunta_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Option extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "pregunta_id")
    private Question pregunta;

    @Column(nullable = false, columnDefinition = "text") private String texto;
    @Column(nullable = false) private boolean esCorrecta = false;
}