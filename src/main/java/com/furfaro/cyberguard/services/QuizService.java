package com.furfaro.cyberguard.services;

import com.furfaro.cyberguard.dto.*;
import com.furfaro.cyberguard.exception.ResourceNotFoundException;
import com.furfaro.cyberguard.exception.BadRequestException;
import com.furfaro.cyberguard.models.*;
import com.furfaro.cyberguard.models.Module;
import com.furfaro.cyberguard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final ModuleProgressRepository moduleProgressRepository;
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserProgressRepository userProgressRepository;

    @Transactional
    public QuizResultDto submitQuiz(Long moduleId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));

        // Verificar que es un módulo de cuestionario final
        if (module.getModuleType() != ModuleType.CUESTIONARIO_FINAL) {
            throw new BadRequestException("This module is not a final quiz");
        }

        ModuleProgress moduleProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module progress not found"));

        // Obtener todas las preguntas del quiz
        List<Question> questions = questionRepository.findByModuleIdOrderByOrderIndexAsc(moduleId);
        
        // Obtener respuestas del usuario
        List<UserAnswer> userAnswers = userAnswerRepository.findByUserIdAndModuleId(user.getId(), moduleId);

        // Verificar que el usuario respondió todas las preguntas
        if (userAnswers.size() < questions.size()) {
            throw new BadRequestException("Please answer all questions before submitting the quiz");
        }

        // Calcular resultados
        int correctAnswers = (int) userAnswers.stream()
                .mapToLong(answer -> answer.getIsCorrect() ? 1 : 0)
                .sum();
        
        int totalPoints = userAnswers.stream()
                .mapToInt(UserAnswer::getPointsEarned)
                .sum();
        
        int maxPoints = questions.stream()
                .mapToInt(Question::getPoints)
                .sum();

        double percentage = maxPoints > 0 ? (double) totalPoints / maxPoints * 100.0 : 0.0;

        // Determinar si aprobó (usando el passing score del curso)
        Course course = module.getCourse();
        boolean passed = percentage >= course.getPassingScore();

        // Actualizar progreso del módulo
        moduleProgress.setStatus(ProgressStatus.COMPLETED);
        moduleProgress.setCompletionPercentage(100.0);
        moduleProgress.setScore(totalPoints);
        moduleProgress.setMaxScore(maxPoints);
        moduleProgress.setCompletedAt(LocalDateTime.now());
        moduleProgressRepository.save(moduleProgress);

        // Actualizar progreso general del curso
        updateCourseProgress(course.getId(), user.getId(), passed, (int) percentage);

        // Construir resultados detallados
        List<QuestionResultDto> questionResults = questions.stream()
                .map(question -> buildQuestionResult(question, userAnswers, user))
                .collect(Collectors.toList());

        String feedback = generateFeedback(percentage, passed, course.getPassingScore());

        return QuizResultDto.builder()
                .moduleId(moduleId)
                .totalQuestions(questions.size())
                .correctAnswers(correctAnswers)
                .totalPoints(totalPoints)
                .maxPoints(maxPoints)
                .percentage(percentage)
                .passed(passed)
                .feedback(feedback)
                .questionResults(questionResults)
                .build();
    }

    public QuizResultDto getQuizResult(Long moduleId, String userEmail) {
        User user = getUserByEmail(userEmail);
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));

        ModuleProgress moduleProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module progress not found"));

        if (moduleProgress.getStatus() != ProgressStatus.COMPLETED) {
            throw new BadRequestException("Quiz has not been completed yet");
        }

        List<Question> questions = questionRepository.findByModuleIdOrderByOrderIndexAsc(moduleId);
        List<UserAnswer> userAnswers = userAnswerRepository.findByUserIdAndModuleId(user.getId(), moduleId);

        int correctAnswers = (int) userAnswers.stream()
                .mapToLong(answer -> answer.getIsCorrect() ? 1 : 0)
                .sum();

        double percentage = moduleProgress.getMaxScore() > 0 ? 
                (double) moduleProgress.getScore() / moduleProgress.getMaxScore() * 100.0 : 0.0;

        Course course = module.getCourse();
        boolean passed = percentage >= course.getPassingScore();

        List<QuestionResultDto> questionResults = questions.stream()
                .map(question -> buildQuestionResult(question, userAnswers, user))
                .collect(Collectors.toList());

        String feedback = generateFeedback(percentage, passed, course.getPassingScore());

        return QuizResultDto.builder()
                .moduleId(moduleId)
                .totalQuestions(questions.size())
                .correctAnswers(correctAnswers)
                .totalPoints(moduleProgress.getScore())
                .maxPoints(moduleProgress.getMaxScore())
                .percentage(percentage)
                .passed(passed)
                .feedback(feedback)
                .questionResults(questionResults)
                .build();
    }

    private void updateCourseProgress(Long courseId, Long userId, boolean passed, int finalScore) {
        UserProgress userProgress = userProgressRepository
                .findByUserIdAndCourseId(userId, courseId)
                .orElse(null);

        if (userProgress != null) {
            // Verificar si todos los módulos están completados
            List<ModuleProgress> allModuleProgress = moduleProgressRepository
                    .findByUserIdAndCourseIdOrderByModuleIndex(userId, courseId);
            
            boolean allModulesCompleted = allModuleProgress.stream()
                    .allMatch(mp -> mp.getStatus() == ProgressStatus.COMPLETED);

            if (allModulesCompleted) {
                userProgress.setStatus(ProgressStatus.COMPLETED);
                userProgress.setProgressPercentage(100.0);
                userProgress.setFinalScore(finalScore);
                userProgress.setPassed(passed);
                userProgress.setCompletedAt(LocalDateTime.now());
                
                userProgressRepository.save(userProgress);
            }
        }
    }

    private QuestionResultDto buildQuestionResult(Question question, List<UserAnswer> userAnswers, User user) {
        UserAnswer userAnswer = userAnswers.stream()
                .filter(answer -> answer.getQuestion().getId().equals(question.getId()))
                .findFirst()
                .orElse(null);

        String userAnswerText = "";
        String correctAnswerText = "";

        // Obtener texto de la respuesta del usuario
        if (userAnswer != null) {
            if (userAnswer.getSelectedOption() != null) {
                userAnswerText = userAnswer.getSelectedOption().getOptionText();
            } else if (userAnswer.getTextAnswer() != null) {
                userAnswerText = userAnswer.getTextAnswer();
            }
        }

        // Obtener texto de la respuesta correcta
        QuestionOption correctOption = questionOptionRepository.findByQuestionIdAndIsCorrectTrue(question.getId());
        if (correctOption != null) {
            correctAnswerText = correctOption.getOptionText();
        }

        return QuestionResultDto.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .isCorrect(userAnswer != null && userAnswer.getIsCorrect())
                .pointsEarned(userAnswer != null ? userAnswer.getPointsEarned() : 0)
                .maxPoints(question.getPoints())
                .userAnswer(userAnswerText)
                .correctAnswer(correctAnswerText)
                .explanation(question.getExplanation())
                .build();
    }

    private String generateFeedback(double percentage, boolean passed, int passingScore) {
        if (passed) {
            if (percentage >= 90) {
                return "¡Excelente trabajo! Has demostrado un dominio sobresaliente de los conceptos de ingeniería social. " +
                       "Tu puntuación de " + String.format("%.1f", percentage) + "% muestra que estás bien preparado para " +
                       "identificar y evitar estos tipos de ataques en la vida real.";
            } else if (percentage >= 80) {
                return "¡Muy bien hecho! Has aprobado el curso con una puntuación de " + String.format("%.1f", percentage) + "%. " +
                       "Tienes un buen entendimiento de los conceptos básicos de ingeniería social. " +
                       "Considera repasar algunas áreas para fortalecer aún más tus conocimientos.";
            } else {
                return "¡Felicitaciones! Has aprobado el curso con " + String.format("%.1f", percentage) + "%. " +
                       "Has demostrado un entendimiento básico de los conceptos de ingeniería social. " +
                       "Te recomendamos continuar practicando para mejorar tus habilidades de detección.";
            }
        } else {
            return "Tu puntuación de " + String.format("%.1f", percentage) + "% no alcanza el mínimo requerido de " + passingScore + "% " +
                   "para aprobar el curso. No te preocupes, puedes volver a intentar el cuestionario. " +
                   "Te sugerimos revisar el material del curso y prestar especial atención a las explicaciones " +
                   "de las preguntas que respondiste incorrectamente.";
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
