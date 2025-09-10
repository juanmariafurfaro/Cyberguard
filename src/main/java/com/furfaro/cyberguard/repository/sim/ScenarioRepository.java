package com.furfaro.cyberguard.repository.sim;

import com.furfaro.cyberguard.models.sim.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findBySimulacionIdOrderByOrdenAsc(Long simulacionId);
}