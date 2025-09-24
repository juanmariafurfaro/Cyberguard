package com.furfaro.cyberguard.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "module_progress",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "module_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.NOT_STARTED;
    
    @Column(name = "completion_percentage", nullable = false)
    private Double completionPercentage = 0.0;
    
    @Column(name = "score")
    private Integer score; // Puntuación obtenida en este módulo
    
    @Column(name = "max_score")
    private Integer maxScore; // Puntuación máxima posible
    
    @Column(name = "attempts")
    private Integer attempts = 0;
    
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime lastAccessedAt;
    
    @PrePersist
    protected void onCreate() {
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
        lastAccessedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastAccessedAt = LocalDateTime.now();
        if (status == ProgressStatus.COMPLETED && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }
}