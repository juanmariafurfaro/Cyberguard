package com.furfaro.cyberguard.models.quiz;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.user.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "respuestas_quiz", indexes = {
        @Index(name="ix_respuestaquiz_usuario", columnList = "usuario_id"),
        @Index(name="ix_respuestaquiz_pregunta", columnList = "pregunta_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizAnswer extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "pregunta_id")
    private Question pregunta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "opcion_id")
    private Option opcion;

    @Column(nullable = false) private boolean correcta;
    @Column(nullable = false) private Instant fecha;
}