package com.furfaro.cyberguard.models.course;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "modulos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Module extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id")
    private Course curso;

    @Column(nullable = false, length = 140) private String nombre;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ModuleType tipo; // TEORIA/SIMULACION/QUIZ
    @Column(nullable = false) private int orden;
    @Column(nullable = false) private boolean isActive = true;
}
