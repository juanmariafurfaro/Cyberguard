package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.services.run.EvaluationService;
import com.furfaro.cyberguard.services.run.dto.EvaluationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping("/attempt/{attemptId}")
    public EvaluationDTO evaluate(@PathVariable Long attemptId) {
        return evaluationService.evaluateAttempt(attemptId);
    }
}
