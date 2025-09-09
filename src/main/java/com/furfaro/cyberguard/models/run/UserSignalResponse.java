package com.furfaro.cyberguard.models.run;

import com.furfaro.cyberguard.models.common.BaseEntity;
import com.furfaro.cyberguard.models.signal.Signal;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "respuesta_usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserSignalResponse extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "intento_id")
    private Attempt intento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "senial_id")
    private Signal senial;

    @Column(nullable = false) private boolean detectada;
    @Column(columnDefinition = "text") private String comentario;
}