package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.models.run.Attempt;
import com.furfaro.cyberguard.services.run.AttemptService;
import com.furfaro.cyberguard.services.run.dto.InteractionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {
    private final AttemptService attemptService;

    @PostMapping("/start")
    public Attempt start(@RequestParam Long userId, @RequestParam Long scenarioId) {
        return attemptService.startAttempt(userId, scenarioId);
    }

    @PostMapping("/{attemptId}/interaction")
    public void record(@PathVariable Long attemptId, @RequestBody InteractionDTO dto) {
        attemptService.recordInteraction(attemptId, dto);
    }

    @PostMapping("/{attemptId}/signal")
    public void upsertSignal(
            @PathVariable Long attemptId,
            @RequestParam Long signalId,
            @RequestParam boolean detectada,
            @RequestParam(required = false) String comentario) {
        attemptService.upsertSignalResponse(attemptId, signalId, detectada, comentario);
    }

    @PostMapping("/{attemptId}/close")
    public Attempt close(@PathVariable Long attemptId) {
        return attemptService.closeAttempt(attemptId);
    }
}