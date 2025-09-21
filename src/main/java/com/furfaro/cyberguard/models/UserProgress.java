package com.furfaro.cyberguard.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @Column(name = "current_module")
    private Integer currentModule = 1;
    
    @Column(name = "completion_percentage")
    private Double completionPercentage = 0.0;
    
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
    
    @Column(name = "final_score")
    private Double finalScore;
}
