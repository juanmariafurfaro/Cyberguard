package com.furfaro.cyberguard.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;
    
    @Column(name = "order_index")
    private Integer orderIndex;
    
    @Column(nullable = false)
    private Integer points = 1; // Puntos que vale la pregunta
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<QuestionOption> options = new ArrayList<>();
    
    // Para actividades de phishing: contenido del email
    @Column(columnDefinition = "TEXT")
    private String emailContent;
    
    @Column(name = "sender_email")
    private String senderEmail;
    
    @Column(name = "email_subject")
    private String emailSubject;
    
    // Para actividades de pretexting: datos del perfil y conversación
    @Column(columnDefinition = "TEXT")
    private String profileData; // JSON con datos del perfil
    
    @Column(columnDefinition = "TEXT")
    private String conversationData; // JSON con la conversación
    
    @Column(columnDefinition = "TEXT")
    private String explanation; // Explicación de la respuesta correcta
    
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
