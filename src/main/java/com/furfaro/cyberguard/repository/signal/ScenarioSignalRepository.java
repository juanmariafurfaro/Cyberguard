package com.furfaro.cyberguard.repository.signal;

import com.furfaro.cyberguard.models.signal.ScenarioSignal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioSignalRepository extends JpaRepository<ScenarioSignal, Long> {
    List<ScenarioSignal> findByEscenarioId(Long escenarioId);
    boolean existsByEscenarioIdAndSenialId(Long escenarioId, Long senialId);
    void deleteByEscenarioIdAndSenialId(Long escenarioId, Long senialId);
}