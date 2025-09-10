package com.furfaro.cyberguard.repository.content;

import com.furfaro.cyberguard.models.content.ScenarioResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioResourceRepository extends JpaRepository<ScenarioResource, Long> {
    List<ScenarioResource> findByEscenarioId(Long escenarioId);
    boolean existsByEscenarioIdAndRecursoId(Long escenarioId, Long recursoId);
    void deleteByEscenarioIdAndRecursoId(Long escenarioId, Long recursoId);
}