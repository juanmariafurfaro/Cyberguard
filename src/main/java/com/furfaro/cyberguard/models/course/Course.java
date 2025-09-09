package com.furfaro.cyberguard.models.course;


import com.furfaro.cyberguard.models.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "cursos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Course extends BaseEntity {
    @Column(nullable = false, length = 120) private String nombre;   // Phishing, Pretexting
    @Column(columnDefinition = "text") private String descripcion;
    @Column(nullable = false) private boolean isActive = true;
}
