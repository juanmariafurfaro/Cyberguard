package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.services.sim.SimulationService;
import com.furfaro.cyberguard.services.sim.dto.SimulationDTO;
import com.furfaro.cyberguard.services.sim.dto.ScenarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sim")
@RequiredArgsConstructor
public class SimulationController {
    private final SimulationService simulationService;

    @GetMapping("/modules/{moduleId}/simulations")
    public List<SimulationDTO> listSimulations(@PathVariable Long moduleId) {
        return simulationService.listSimulations(moduleId);
    }

    @GetMapping("/simulations/{simulationId}/scenarios")
    public List<ScenarioDTO> listScenarios(@PathVariable Long simulationId) {
        return simulationService.listScenarios(simulationId);
    }
}