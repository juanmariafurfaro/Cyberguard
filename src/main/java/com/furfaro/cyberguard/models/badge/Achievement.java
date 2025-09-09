package com.furfaro.cyberguard.models.badge;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "logros")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Achievement extends BaseEntity {
    @Column(nullable = false, length = 140) private String nombre;
    @Column(columnDefinition = "text") private String descripcion;
}