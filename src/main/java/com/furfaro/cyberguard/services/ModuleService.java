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
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final ModuleProgressRepository moduleProgressRepository;
    private final ModuleContentRepository moduleContentRepository;
    private final QuestionRepository questionRepository;

    public ModuleDto getModuleForUser(Long moduleId, String userEmail) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));
        
        User user = getUserByEmail(userEmail);
        
        // Verificar acceso al módulo
        if (!isModuleAccessibleForUser(module, user)) {
            throw new BadRequestException("Module is not accessible. Complete previous modules first.");
        }

        ModuleProgress moduleProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), moduleId)
                .orElse(null);

        // Obtener contenido del módulo
        List<ModuleContent> contents = moduleContentRepository.findByModuleIdOrderByOrderIndexAsc(moduleId);
        List<ModuleContentDto> contentDtos = contents.stream()
                .map(this::buildModuleContentDto)
                .collect(Collectors.toList());

        // Obtener preguntas del módulo
        List<Question> questions = questionRepository.findByModuleIdOrderByOrderIndexAsc(moduleId);
        List<QuestionDto> questionDtos = questions.stream()
                .map(question -> buildQuestionDto(question, user))
                .collect(Collectors.toList());

        return ModuleDto.builder()
                .id(module.getId())
                .title(module.getTitle())
                .description(module.getDescription())
                .moduleType(module.getModuleType().name())
                .orderIndex(module.getOrderIndex())
                .isRequired(module.getIsRequired())
                .isAccessible(true) // Ya verificamos el acceso arriba
                .isCompleted(moduleProgress != null && moduleProgress.getStatus() == ProgressStatus.COMPLETED)
                .completionPercentage(moduleProgress != null ? moduleProgress.getCompletionPercentage() : 0.0)
                .score(moduleProgress != null ? moduleProgress.getScore() : null)
                .maxScore(moduleProgress != null ? moduleProgress.getMaxScore() : null)
                .contents(contentDtos)
                .questions(questionDtos)
                .courseId(module.getCourse().getId())
                .build();
    }

    @Transactional
    public void startModule(Long moduleId, String userEmail) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));
        
        User user = getUserByEmail(userEmail);

        if (!isModuleAccessibleForUser(module, user)) {
            throw new BadRequestException("Module is not accessible. Complete previous modules first.");
        }

        ModuleProgress moduleProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module progress not found"));

        if (moduleProgress.getStatus() == ProgressStatus.NOT_STARTED) {
            moduleProgress.setStatus(ProgressStatus.IN_PROGRESS);
            moduleProgress.setStartedAt(LocalDateTime.now());
            moduleProgressRepository.save(moduleProgress);
        }
    }

    @Transactional
    public void updateModuleProgress(Long moduleId, String userEmail, ModuleProgressUpdateRequest request) {
        User user = getUserByEmail(userEmail);
        
        ModuleProgress moduleProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module progress not found"));

        if (request.getCompletionPercentage() != null) {
            moduleProgress.setCompletionPercentage(request.getCompletionPercentage());
        }

        if (request.getStatus() != null) {
            ProgressStatus status = ProgressStatus.valueOf(request.getStatus());
            moduleProgress.setStatus(status);
            
            if (status == ProgressStatus.COMPLETED && moduleProgress.getCompletedAt() == null) {
                moduleProgress.setCompletedAt(LocalDateTime.now());
            }
        }

        moduleProgressRepository.save(moduleProgress);
    }

    @Transactional
    public void completeModule(Long moduleId, String userEmail) {
        User user = getUserByEmail(userEmail);
        
        ModuleProgress moduleProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module progress not found"));

        moduleProgress.setStatus(ProgressStatus.COMPLETED);
        moduleProgress.setCompletionPercentage(100.0);
        moduleProgress.setCompletedAt(LocalDateTime.now());
        
        moduleProgressRepository.save(moduleProgress);
    }

    private boolean isModuleAccessibleForUser(Module module, User user) {
        // El primer módulo siempre es accesible
        if (module.getOrderIndex() == 0) {
            return true;
        }

        // Buscar el módulo anterior en el mismo curso
        Module previousModule = moduleRepository.findByCourseIdAndOrderIndex(
                module.getCourse().getId(), 
                module.getOrderIndex() - 1
        );

        if (previousModule == null) {
            return true; // No hay módulo anterior
        }

        // Verificar que el módulo anterior esté completado
        ModuleProgress previousProgress = moduleProgressRepository
                .findByUserIdAndModuleId(user.getId(), previousModule.getId())
                .orElse(null);

        return previousProgress != null && previousProgress.getStatus() == ProgressStatus.COMPLETED;
    }

    private ModuleContentDto buildModuleContentDto(ModuleContent content) {
        return ModuleContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .contentType(content.getContentType().name())
                .textContent(content.getTextContent())
                .mediaUrl(content.getMediaUrl())
                .mediaDescription(content.getMediaDescription())
                .orderIndex(content.getOrderIndex())
                .metadata(content.getMetadata())
                .build();
    }

    private QuestionDto buildQuestionDto(Question question, User user) {
        return QuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .questionType(question.getQuestionType().name())
                .orderIndex(question.getOrderIndex())
                .points(question.getPoints())
                .emailContent(question.getEmailContent())
                .senderEmail(question.getSenderEmail())
                .emailSubject(question.getEmailSubject())
                .profileData(question.getProfileData())
                .conversationData(question.getConversationData())
                .explanation(question.getExplanation())
                .build();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
