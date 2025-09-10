package com.furfaro.cyberguard.repository.progress;

import com.furfaro.cyberguard.models.progress.ModuleProgress;
import com.furfaro.cyberguard.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Long> {
    List<ModuleProgress> findByUsuarioId(Long usuarioId);
    Optional<ModuleProgress> findByUsuarioIdAndModuloId(Long usuarioId, Long moduloId);
    boolean existsByUsuarioIdAndModuloId(Long usuarioId, Long moduloId);
}
