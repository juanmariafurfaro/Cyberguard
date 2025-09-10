package com.furfaro.cyberguard.repository.course;

import com.furfaro.cyberguard.models.course.Module;
import com.furfaro.cyberguard.models.course.ModuleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCursoIdOrderByOrdenAsc(Long cursoId);
    List<Module> findByCursoIdAndTipoOrderByOrdenAsc(Long cursoId, ModuleType tipo);
}