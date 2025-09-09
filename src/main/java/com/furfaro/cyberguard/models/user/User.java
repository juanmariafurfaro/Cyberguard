package com.furfaro.cyberguard.models.user;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "usuarios", indexes = {
        @Index(name = "ix_usuario_email", columnList = "email", unique = true)
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {
    @Column(nullable = false, length = 120) private String nombre;
    @Column(nullable = false, length = 140, unique = true) private String email;
    @Column(nullable = false) private String contraseniaHash; // guarda hash, no texto plano
    @Column(nullable = false) private java.time.LocalDate fechaRegistro;
}