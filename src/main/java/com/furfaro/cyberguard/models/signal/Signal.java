package com.furfaro.cyberguard.models.signal;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "seniales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Signal extends BaseEntity {
    @Enumerated(EnumType.STRING) @Column(nullable = false) private SignalCategory categoria;
    @Column(columnDefinition = "text") private String descripcion;
}