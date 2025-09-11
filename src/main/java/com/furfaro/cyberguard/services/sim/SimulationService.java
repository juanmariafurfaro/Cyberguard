package com.furfaro.cyberguard.services.sim;

import com.furfaro.cyberguard.models.sim.Scenario;
import com.furfaro.cyberguard.models.sim.Simulation;
import com.furfaro.cyberguard.repository.sim.ScenarioRepository;
import com.furfaro.cyberguard.repository.sim.SimulationRepository;
import com.furfaro.cyberguard.services.sim.dto.ScenarioDTO;
import com.furfaro.cyberguard.services.sim.dto.SimulationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final ScenarioRepository scenarioRepository;

    // --- Mappers básicos ---
    private SimulationDTO toDto(Simulation s) {
        return SimulationDTO.builder()
                .id(s.getId())
                .moduloId(s.getModulo().getId())
                .nombre(s.getNombre())
                .descripcion(s.getDescripcion())
                .tipo(s.getTipo())
                .orden(s.getOrden())
                .active(s.isActive())
                .build();
    }

    private ScenarioDTO toDto(Scenario e) {
        return ScenarioDTO.builder()
                .id(e.getId())
                .simulacionId(e.getSimulacion().getId())
                .nombre(e.getNombre())
                .descripcion(e.getDescripcion())
                .orden(e.getOrden())
                .dificultad(e.getDificultad())
                .active(e.isActive())
                .build();
    }

    // --- API ---

    /** Simulaciones de un módulo (ordenadas). */
    @Cacheable(value = "module_simulations", key = "#moduleId")
    public List<SimulationDTO> listSimulations(Long moduleId) {
        return simulationRepository.findByModuloIdOrderByOrdenAsc(moduleId)
                .stream().map(this::toDto).toList();
    }

    /** Escenarios de una simulación (solo info básica; sin gabarito). */
    @Cacheable(value = "simulation_scenarios", key = "#simulationId")
    public List<ScenarioDTO> listScenarios(Long simulationId) {
        return scenarioRepository.findBySimulacionIdOrderByOrdenAsc(simulationId)
                .stream().map(this::toDto).toList();
    }
}