package com.furfaro.cyberguard.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "module_contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleContent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;
    
    @Column(columnDefinition = "TEXT")
    private String textContent;
    
    @Column(name = "media_url")
    private String mediaUrl; // URL para imágenes, videos, etc.
    
    @Column(name = "media_description")
    private String mediaDescription;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
    
    @Column(columnDefinition = "TEXT")
    private String metadata; // JSON para configuraciones adicionales
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
