package com.furfaro.cyberguard.repository.run;

import com.furfaro.cyberguard.models.run.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    Optional<Evaluation> findByIntentoId(Long intentoId);
    boolean existsByIntentoId(Long intentoId);
}