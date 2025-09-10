package com.furfaro.cyberguard.repository.sim;

import com.furfaro.cyberguard.models.sim.Simulation;
import com.furfaro.cyberguard.models.sim.SimulationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    List<Simulation> findByModuloIdOrderByOrdenAsc(Long moduloId);
    List<Simulation> findByModuloIdAndTipoOrderByOrdenAsc(Long moduloId, SimulationType tipo);
}