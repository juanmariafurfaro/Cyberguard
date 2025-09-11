package com.furfaro.cyberguard.services.progress;

import com.furfaro.cyberguard.models.progress.ModuleProgress;
import com.furfaro.cyberguard.models.progress.ProgressState;
import com.furfaro.cyberguard.repository.progress.ModuleProgressRepository;
import com.furfaro.cyberguard.repository.course.ModuleRepository;
import com.furfaro.cyberguard.repository.user.UserRepository;  
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ModuleProgressRepository moduleProgressRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;           

    /** Devuelve el progreso existente o lo crea en NO_INICIADO (0%). */
    @Transactional
    public ModuleProgress getOrCreate(Long userId, Long moduleId) {
        return moduleProgressRepository.findByUsuarioIdAndModuloId(userId, moduleId)
                .orElseGet(() -> {
                    moduleRepository.findById(moduleId)
                            .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado: " + moduleId));

                    ModuleProgress mp = ModuleProgress.builder()
                            .usuario(userRepository.getReferenceById(userId))     // ✅ usa proxy por id
                            .modulo(moduleRepository.getReferenceById(moduleId))  // ✅ usa proxy por id
                            .estado(ProgressState.NO_INICIADO)
                            .porcentaje(0)
                            .updatedAtClient(Instant.now())
                            .build();

                    return moduleProgressRepository.save(mp);
                });
    }

    /** Marca EN_CURSO si estaba NO_INICIADO (idempotente). */
    @Transactional
    public void markInProgress(Long userId, Long moduleId) {
        ModuleProgress mp = getOrCreate(userId, moduleId);
        if (mp.getEstado() == ProgressState.NO_INICIADO) {
            mp.setEstado(ProgressState.EN_CURSO);
            mp.setUpdatedAtClient(Instant.now());
            moduleProgressRepository.save(mp);
        }
    }

    /** Actualiza porcentaje (0..100) y pone EN_CURSO/COMPLETADO según corresponda. */
    @Transactional
    public void updatePercentage(Long userId, Long moduleId, int porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje inválido (0..100)");
        }
        ModuleProgress mp = getOrCreate(userId, moduleId);
        mp.setPorcentaje(porcentaje);
        if (porcentaje > 0 && mp.getEstado() == ProgressState.NO_INICIADO) {
            mp.setEstado(ProgressState.EN_CURSO);
        }
        if (porcentaje == 100) {
            mp.setEstado(ProgressState.COMPLETADO);
        }
        mp.setUpdatedAtClient(Instant.now());
        moduleProgressRepository.save(mp);
    }

    /** Marca COMPLETADO y porcentaje=100. */
    @Transactional
    public void markCompleted(Long userId, Long moduleId) {
        ModuleProgress mp = getOrCreate(userId, moduleId);
        mp.setEstado(ProgressState.COMPLETADO);
        mp.setPorcentaje(100);
        mp.setUpdatedAtClient(Instant.now());
        moduleProgressRepository.save(mp);
    }
}

