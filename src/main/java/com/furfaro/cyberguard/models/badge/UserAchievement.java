package com.furfaro.cyberguard.models.badge;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "usuario_logro",
        uniqueConstraints = @UniqueConstraint(name="uk_usuario_logro", columnNames = {"usuario_id","logro_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAchievement extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "logro_id")
    private Achievement logro;

    @Column(nullable = false) private Instant fechaObtencion;
}