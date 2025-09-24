package com.furfaro.cyberguard.controllers;


import com.furfaro.cyberguard.dto.*;
import com.furfaro.cyberguard.services.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/module/{moduleId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<QuestionDto>> getQuestionsForModule(
            @PathVariable Long moduleId, 
            Authentication authentication) {
        String email = authentication.getName();
        List<QuestionDto> questions = questionService.getQuestionsForModule(moduleId, email);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/answer")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SubmitAnswerResponse> submitAnswer(
            @Valid @RequestBody SubmitAnswerRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        SubmitAnswerResponse response = questionService.submitAnswer(request, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/phishing/{questionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PhishingEmailDto> getPhishingEmail(@PathVariable Long questionId, Authentication authentication) {
        String email = authentication.getName();
        PhishingEmailDto phishingEmail = questionService.getPhishingEmail(questionId, email);
        return ResponseEntity.ok(phishingEmail);
    }

    @GetMapping("/pretexting/{questionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PretextingConversationDto> getPretextingConversation(
            @PathVariable Long questionId, 
            Authentication authentication) {
        String email = authentication.getName();
        PretextingConversationDto conversation = questionService.getPretextingConversation(questionId, email);
        return ResponseEntity.ok(conversation);
    }
}