package com.furfaro.cyberguard.models.content;

import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "recursos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resource extends BaseEntity {
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ResourceType tipo;
    @Column(nullable = false, length = 512) private String url;        // o path local
    @Column(columnDefinition = "text") private String transcript;      // accesibilidad (audio/video)
    @Column(length = 512) private String altText;                      // accesibilidad (imagen)
}