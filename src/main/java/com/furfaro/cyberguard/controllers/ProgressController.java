package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.models.progress.ModuleProgress;
import com.furfaro.cyberguard.services.progress.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;

    @GetMapping
    public ModuleProgress get(@RequestParam Long userId, @RequestParam Long moduleId) {
        return progressService.getOrCreate(userId, moduleId);
    }

    @PostMapping("/in-progress")
    public void markInProgress(@RequestParam Long userId, @RequestParam Long moduleId) {
        progressService.markInProgress(userId, moduleId);
    }

    @PostMapping("/percent")
    public void updatePercentage(@RequestParam Long userId, @RequestParam Long moduleId, @RequestParam int porcentaje) {
        progressService.updatePercentage(userId, moduleId, porcentaje);
    }

    @PostMapping("/complete")
    public void complete(@RequestParam Long userId, @RequestParam Long moduleId) {
        progressService.markCompleted(userId, moduleId);
    }
}
