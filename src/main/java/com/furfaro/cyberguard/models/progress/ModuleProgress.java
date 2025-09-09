package com.furfaro.cyberguard.models.progress;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.course.Module;
import com.furfaro.cyberguard.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "progreso_modulo",
        uniqueConstraints = @UniqueConstraint(name="uk_progreso_usuario_modulo", columnNames = {"usuario_id","modulo_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ModuleProgress extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "modulo_id")
    private Module modulo;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ProgressState estado = ProgressState.NO_INICIADO;

    @Column(nullable = false) private int porcentaje = 0;
    @Column(nullable = false) private Instant updatedAtClient; // opcional: última actualización desde cliente
}
