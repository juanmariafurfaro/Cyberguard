package com.furfaro.cyberguard.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCertificate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @Column(name = "certificate_code", unique = true)
    private String certificateCode;
    
    @Column(name = "issued_date")
    private LocalDateTime issuedDate = LocalDateTime.now();
    
    @Column(name = "final_score")
    private Double finalScore;
}