package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.services.quiz.QuizService;
import com.furfaro.cyberguard.services.quiz.dto.QuestionDTO;
import com.furfaro.cyberguard.services.quiz.dto.QuizResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/module/{moduleId}")
    public List<QuestionDTO> getQuiz(@PathVariable Long moduleId) {
        return quizService.getQuiz(moduleId);
    }

    @PostMapping("/answer")
    public QuizResultDTO answer(
            @RequestParam Long userId,
            @RequestParam Long questionId,
            @RequestParam Long optionId) {
        return quizService.answer(userId, questionId, optionId);
    }
}