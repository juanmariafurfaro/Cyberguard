package com.furfaro.cyberguard.controllers;


import com.furfaro.cyberguard.dto.QuizResultDto;
import com.furfaro.cyberguard.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/module/{moduleId}/submit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<QuizResultDto> submitQuiz(@PathVariable Long moduleId, Authentication authentication) {
        String email = authentication.getName();
        QuizResultDto result = quizService.submitQuiz(moduleId, email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/module/{moduleId}/result")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<QuizResultDto> getQuizResult(@PathVariable Long moduleId, Authentication authentication) {
        String email = authentication.getName();
        QuizResultDto result = quizService.getQuizResult(moduleId, email);
        return ResponseEntity.ok(result);
    }
}