package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.services.sim.ScenarioContentService;
import com.furfaro.cyberguard.services.sim.dto.ScenarioViewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenarios")
@RequiredArgsConstructor
public class ScenarioContentController {
    private final ScenarioContentService scenarioContentService;

    @GetMapping("/{scenarioId}/view")
    public ScenarioViewDTO getForPlay(@PathVariable Long scenarioId) {
        return scenarioContentService.getScenarioForPlay(scenarioId);
    }
}