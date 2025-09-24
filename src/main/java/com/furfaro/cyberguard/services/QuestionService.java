package com.furfaro.cyberguard.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furfaro.cyberguard.dto.*;
import com.furfaro.cyberguard.exception.ResourceNotFoundException;
import com.furfaro.cyberguard.exception.BadRequestException;
import com.furfaro.cyberguard.models.*;
import com.furfaro.cyberguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final ModuleProgressRepository moduleProgressRepository;
    private final ObjectMapper objectMapper;

    public List<QuestionDto> getQuestionsForModule(Long moduleId, String userEmail) {
        User user = getUserByEmail(userEmail);
        List<Question> questions = questionRepository.findByModuleIdOrderByOrderIndexAsc(moduleId);
        
        return questions.stream()
                .map(question -> buildQuestionDto(question, user))
                .collect(Collectors.toList());
    }

    @Transactional
    public SubmitAnswerResponse submitAnswer(SubmitAnswerRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        // Verificar que el usuario tenga acceso a este módulo
        ModuleProgress moduleProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), question.getModule().getId())
                .orElseThrow(() -> new BadRequestException("Module not accessible"));

        // Buscar respuesta existente o crear nueva
        int attemptNumber = request.getAttemptNumber() != null ? request.getAttemptNumber() : 1;
        UserAnswer userAnswer = userAnswerRepository
                .findByUserIdAndQuestionIdAndAttemptNumber(user.getId(), request.getQuestionId(), attemptNumber)
                .orElse(new UserAnswer());

        if (userAnswer.getId() == null) {
            userAnswer.setUser(user);
            userAnswer.setQuestion(question);
            userAnswer.setAttemptNumber(attemptNumber);
        }

        // Evaluar la respuesta
        boolean isCorrect = false;
        String correctAnswer = "";
        int pointsEarned = 0;

        if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE || 
            question.getQuestionType() == QuestionType.TRUE_FALSE ||
            question.getQuestionType() == QuestionType.PHISHING_EMAIL ||
            question.getQuestionType() == QuestionType.PRETEXTING_CONVERSATION) {
            
            if (request.getSelectedOptionId() != null) {
                QuestionOption selectedOption = questionOptionRepository.findById(request.getSelectedOptionId())
                        .orElseThrow(() -> new BadRequestException("Invalid option selected"));
                
                QuestionOption correctOption = questionOptionRepository.findByQuestionIdAndIsCorrectTrue(request.getQuestionId());
                
                isCorrect = selectedOption.getIsCorrect();
                correctAnswer = correctOption != null ? correctOption.getOptionText() : "";
                pointsEarned = isCorrect ? question.getPoints() : 0;
                
                userAnswer.setSelectedOption(selectedOption);
            }
        } else if (request.getTextAnswer() != null) {
            userAnswer.setTextAnswer(request.getTextAnswer());
            // Lógica adicional para respuestas de texto si es necesario
        }

        userAnswer.setIsCorrect(isCorrect);
        userAnswer.setPointsEarned(pointsEarned);
        userAnswerRepository.save(userAnswer);

        // Actualizar progreso del módulo si es necesario
        updateModuleProgressAfterAnswer(moduleProgress, question.getModule().getId(), user.getId());

        return SubmitAnswerResponse.builder()
                .isCorrect(isCorrect)
                .pointsEarned(pointsEarned)
                .totalPoints(question.getPoints())
                .explanation(question.getExplanation())
                .correctAnswer(correctAnswer)
                .build();
    }

    public PhishingEmailDto getPhishingEmail(Long questionId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (question.getQuestionType() != QuestionType.PHISHING_EMAIL) {
            throw new BadRequestException("Question is not a phishing email type");
        }

        List<QuestionOption> options = questionOptionRepository.findByQuestionIdOrderByOrderIndexAsc(questionId);
        List<String> optionTexts = options.stream()
                .map(QuestionOption::getOptionText)
                .collect(Collectors.toList());

        return PhishingEmailDto.builder()
                .questionId(question.getId())
                .senderEmail(question.getSenderEmail())
                .emailSubject(question.getEmailSubject())
                .emailContent(question.getEmailContent())
                .options(optionTexts)
                .explanation(question.getExplanation())
                .build();
    }

    public PretextingConversationDto getPretextingConversation(Long questionId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (question.getQuestionType() != QuestionType.PRETEXTING_CONVERSATION) {
            throw new BadRequestException("Question is not a pretexting conversation type");
        }

        try {
            // Parsear datos del perfil
            ProfileDto profile = objectMapper.readValue(question.getProfileData(), ProfileDto.class);
            
            // Parsear conversación
            List<ConversationMessageDto> conversation = objectMapper.readValue(
                question.getConversationData(), 
                new TypeReference<List<ConversationMessageDto>>() {}
            );

            List<QuestionOption> options = questionOptionRepository.findByQuestionIdOrderByOrderIndexAsc(questionId);
            List<String> optionTexts = options.stream()
                    .map(QuestionOption::getOptionText)
                    .collect(Collectors.toList());

            return PretextingConversationDto.builder()
                    .questionId(question.getId())
                    .profile(profile)
                    .conversation(conversation)
                    .options(optionTexts)
                    .explanation(question.getExplanation())
                    .build();
        } catch (Exception e) {
            throw new BadRequestException("Error parsing conversation data: " + e.getMessage());
        }
    }

    private QuestionDto buildQuestionDto(Question question, User user) {
        List<QuestionOption> options = questionOptionRepository.findByQuestionIdOrderByOrderIndexAsc(question.getId());
        List<QuestionOptionDto> optionDtos = options.stream()
                .map(this::buildQuestionOptionDto)
                .collect(Collectors.toList());

        // Verificar si el usuario ya respondió esta pregunta
        List<UserAnswer> userAnswers = userAnswerRepository.findByUserIdAndQuestionId(user.getId(), question.getId());
        boolean isAnswered = !userAnswers.isEmpty();
        Boolean isCorrect = null;
        Integer userAnswerId = null;

        if (isAnswered) {
            UserAnswer latestAnswer = userAnswers.stream()
                    .max((a, b) -> a.getAttemptNumber().compareTo(b.getAttemptNumber()))
                    .orElse(null);
            
            if (latestAnswer != null) {
                isCorrect = latestAnswer.getIsCorrect();
                if (latestAnswer.getSelectedOption() != null) {
                    userAnswerId = latestAnswer.getSelectedOption().getId().intValue();
                }
            }
        }

        return QuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType().name())
                .orderIndex(question.getOrderIndex())
                .points(question.getPoints())
                .options(optionDtos)
                .emailContent(question.getEmailContent())
                .senderEmail(question.getSenderEmail())
                .emailSubject(question.getEmailSubject())
                .profileData(question.getProfileData())
                .conversationData(question.getConversationData())
                .explanation(question.getExplanation())
                .isAnswered(isAnswered)
                .isCorrect(isCorrect)
                .userAnswer(userAnswerId)
                .build();
    }

    private QuestionOptionDto buildQuestionOptionDto(QuestionOption option) {
        return QuestionOptionDto.builder()
                .id(option.getId())
                .optionText(option.getOptionText())
                .orderIndex(option.getOrderIndex())
                // No incluir isCorrect aquí por seguridad
                .build();
    }

    private void updateModuleProgressAfterAnswer(ModuleProgress moduleProgress, Long moduleId, Long userId) {
        // Contar total de preguntas en el módulo
        List<Question> allQuestions = questionRepository.findByModuleIdOrderByOrderIndexAsc(moduleId);
        
        // Contar respuestas correctas del usuario
        List<UserAnswer> userAnswers = userAnswerRepository.findByUserIdAndModuleId(userId, moduleId);
        long correctAnswers = userAnswers.stream()
                .mapToLong(answer -> answer.getIsCorrect() ? 1 : 0)
                .sum();
        
        // Calcular progreso basado en respuestas
        if (!allQuestions.isEmpty()) {
            double answerProgress = (double) userAnswers.size() / allQuestions.size() * 100.0;
            moduleProgress.setCompletionPercentage(Math.min(answerProgress, 100.0));
            
            // Calcular puntuación
            Integer totalPoints = userAnswerRepository.sumPointsEarnedByUserIdAndModuleId(userId, moduleId).orElse(0);
            Integer maxPossiblePoints = allQuestions.stream()
                    .mapToInt(Question::getPoints)
                    .sum();
            
            moduleProgress.setScore(totalPoints);
            moduleProgress.setMaxScore(maxPossiblePoints);
            
            // Si respondió todas las preguntas, marcar como completado
            if (userAnswers.size() >= allQuestions.size()) {
                moduleProgress.setStatus(ProgressStatus.COMPLETED);
                moduleProgress.setCompletedAt(LocalDateTime.now());
            }
            
            moduleProgressRepository.save(moduleProgress);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}