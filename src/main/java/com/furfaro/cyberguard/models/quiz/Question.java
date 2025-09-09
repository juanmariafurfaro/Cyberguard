package com.furfaro.cyberguard.models.quiz;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.course.Module;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "preguntas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Question extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "modulo_id")
    private Module modulo; // módulo de tipo QUIZ

    @Column(nullable = false, columnDefinition = "text") private String enunciado;
    @Column(nullable = false) private int orden;
}
