package com.furfaro.cyberguard.services.course;


import com.furfaro.cyberguard.models.course.Course;
import com.furfaro.cyberguard.models.course.Module;
import com.furfaro.cyberguard.repository.course.CourseRepository;
import com.furfaro.cyberguard.repository.course.ModuleRepository;
import com.furfaro.cyberguard.services.course.dto.CourseDTO;
import com.furfaro.cyberguard.services.course.dto.ModuleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    // --- Mappers básicos ---
    private CourseDTO toDto(Course c) {
        return CourseDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .active(c.isActive())
                .build();
    }

    private ModuleDTO toDto(Module m) {
        return ModuleDTO.builder()
                .id(m.getId())
                .cursoId(m.getCurso().getId())
                .nombre(m.getNombre())
                .tipo(m.getTipo())
                .orden(m.getOrden())
                .active(m.isActive())
                .build();
    }

    // --- API ---

    /** Lista cursos activos (catálogo). Cacheable: cambia poco. */
    @Cacheable("courses_active")
    public List<CourseDTO> listActiveCourses() {
        return courseRepository.findByIsActiveTrue()
                .stream().map(this::toDto).toList();
    }

    /** Módulos (ordenados) de un curso. */
    @Cacheable(value = "course_modules", key = "#courseId")
    public List<ModuleDTO> listModulesOfCourse(Long courseId) {
        // (Opcional) validar que exista el curso para un 404 si no existe
        courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + courseId));

        return moduleRepository.findByCursoIdOrderByOrdenAsc(courseId)
                .stream().map(this::toDto).toList();
    }

    /** Detalle de un módulo por id. */
    @Cacheable(value = "module_by_id", key = "#moduleId")
    public ModuleDTO getModule(Long moduleId) {
        Module m = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado: " + moduleId));
        return toDto(m);
    }
}