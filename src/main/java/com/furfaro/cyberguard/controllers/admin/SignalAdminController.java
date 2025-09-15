package com.furfaro.cyberguard.controllers.admin;

import com.furfaro.cyberguard.models.signal.Signal;
import com.furfaro.cyberguard.services.admin.SignalAdminService;
import com.furfaro.cyberguard.services.signal.dto.ScenarioSignalDTO;
import com.furfaro.cyberguard.services.signal.dto.SignalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/signals")
@RequiredArgsConstructor
public class SignalAdminController {
    private final SignalAdminService service;

    @PostMapping
    public Signal upsert(@RequestBody SignalDTO dto) { return service.upsertSignal(dto); }

    @PostMapping("/scenario/{scenarioId}/attach")
    public void attach(
            @PathVariable Long scenarioId,
            @RequestParam Long signalId,
            @RequestParam int peso,
            @RequestParam boolean obligatoria) {
        service.attachSignalToScenario(scenarioId, signalId, peso, obligatoria);
    }

    @DeleteMapping("/scenario/{scenarioId}/detach")
    public void detach(@PathVariable Long scenarioId, @RequestParam Long signalId) {
        service.detachSignalFromScenario(scenarioId, signalId);
    }

    @GetMapping("/scenario/{scenarioId}")
    public List<ScenarioSignalDTO> list(@PathVariable Long scenarioId) {
        return service.listScenarioSignals(scenarioId);
    }
}