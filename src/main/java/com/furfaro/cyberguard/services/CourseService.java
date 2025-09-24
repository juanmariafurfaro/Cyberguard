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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleProgressRepository moduleProgressRepository;

    public List<CourseProgressDto> getAllCoursesForUser(String userEmail) {
        User user = getUserByEmail(userEmail);
        List<Course> allCourses = courseRepository.findAll();
        
        return allCourses.stream()
                .map(course -> buildCourseProgressDto(course, user))
                .collect(Collectors.toList());
    }

    public CourseDetailDto getCourseDetail(Long courseId, String userEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        User user = getUserByEmail(userEmail);
        
        // Obtener módulos del curso
        List<Module> modules = moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        
        // Obtener progreso del usuario
        List<ModuleProgress> moduleProgressList = 
                moduleProgressRepository.findByUserIdAndCourseIdOrderByModuleIndex(user.getId(), courseId);
        
        List<ModuleDto> moduleDtos = modules.stream()
                .map(module -> buildModuleDto(module, user, moduleProgressList))
                .collect(Collectors.toList());

        // Calcular progreso general
        double overallProgress = calculateOverallProgress(moduleProgressList, modules.size());
        boolean isCompleted = moduleProgressList.stream()
                .allMatch(mp -> mp.getStatus() == ProgressStatus.COMPLETED);

        return CourseDetailDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .courseType(course.getCourseType().name())
                .passingScore(course.getPassingScore())
                .modules(moduleDtos)
                .overallProgress(overallProgress)
                .isCompleted(isCompleted)
                .build();
    }

    @Transactional
    public void enrollUserInCourse(Long courseId, String userEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        User user = getUserByEmail(userEmail);
        
        // Verificar si ya está inscrito
        if (userProgressRepository.findByUserIdAndCourseId(user.getId(), courseId).isPresent()) {
            throw new BadRequestException("User is already enrolled in this course");
        }

        // Crear progreso general del curso
        UserProgress userProgress = new UserProgress();
        userProgress.setUser(user);
        userProgress.setCourse(course);
        userProgress.setStatus(ProgressStatus.NOT_STARTED);
        userProgressRepository.save(userProgress);

        // Crear progreso para cada módulo
        List<Module> modules = moduleRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        for (Module module : modules) {
            ModuleProgress moduleProgress = new ModuleProgress();
            moduleProgress.setUser(user);
            moduleProgress.setModule(module);
            moduleProgress.setStatus(ProgressStatus.NOT_STARTED);
            moduleProgressRepository.save(moduleProgress);
        }
    }

    private CourseProgressDto buildCourseProgressDto(Course course, User user) {
        UserProgress progress = userProgressRepository
                .findByUserIdAndCourseId(user.getId(), course.getId())
                .orElse(null);

        if (progress == null) {
            return CourseProgressDto.builder()
                    .courseId(course.getId())
                    .courseTitle(course.getTitle())
                    .courseDescription(course.getDescription())
                    .currentModule(0)
                    .completionPercentage(0.0)
                    .isCompleted(false)
                    .build();
        }

        return CourseProgressDto.builder()
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .courseDescription(course.getDescription())
                .currentModule(progress.getCurrentModuleIndex())
                .completionPercentage(progress.getProgressPercentage())
                .isCompleted(progress.getStatus() == ProgressStatus.COMPLETED)
                .finalScore(progress.getFinalScore() != null ? progress.getFinalScore().doubleValue() : null)
                .build();
    }

    private ModuleDto buildModuleDto(Module module, User user, List<ModuleProgress> moduleProgressList) {
        ModuleProgress moduleProgress = moduleProgressList.stream()
                .filter(mp -> mp.getModule().getId().equals(module.getId()))
                .findFirst()
                .orElse(null);

        boolean isAccessible = isModuleAccessible(module, moduleProgressList);
        boolean isCompleted = moduleProgress != null && moduleProgress.getStatus() == ProgressStatus.COMPLETED;
        double completionPercentage = moduleProgress != null ? moduleProgress.getCompletionPercentage() : 0.0;

        return ModuleDto.builder()
                .id(module.getId())
                .title(module.getTitle())
                .description(module.getDescription())
                .moduleType(module.getModuleType().name())
                .orderIndex(module.getOrderIndex())
                .isRequired(module.getIsRequired())
                .isAccessible(isAccessible)
                .isCompleted(isCompleted)
                .completionPercentage(completionPercentage)
                .score(moduleProgress != null ? moduleProgress.getScore() : null)
                .maxScore(moduleProgress != null ? moduleProgress.getMaxScore() : null)
                .build();
    }

    private boolean isModuleAccessible(Module module, List<ModuleProgress> moduleProgressList) {
        // El primer módulo siempre es accesible
        if (module.getOrderIndex() == 0) {
            return true;
        }

        // Verificar que el módulo anterior esté completado
        return moduleProgressList.stream()
                .anyMatch(mp -> mp.getModule().getOrderIndex().equals(module.getOrderIndex() - 1) 
                        && mp.getStatus() == ProgressStatus.COMPLETED);
    }

    private double calculateOverallProgress(List<ModuleProgress> moduleProgressList, int totalModules) {
        if (totalModules == 0) return 0.0;
        
        double totalProgress = moduleProgressList.stream()
                .mapToDouble(ModuleProgress::getCompletionPercentage)
                .sum();
        
        return totalProgress / totalModules;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}