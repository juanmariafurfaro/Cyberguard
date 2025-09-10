package com.furfaro.cyberguard.repository.run;

import com.furfaro.cyberguard.models.run.Attempt;
import com.furfaro.cyberguard.models.run.AttemptState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByUsuarioId(Long usuarioId);
    List<Attempt> findByUsuarioIdAndEstado(Long usuarioId, AttemptState estado);
    List<Attempt> findByEscenarioId(Long escenarioId);
}