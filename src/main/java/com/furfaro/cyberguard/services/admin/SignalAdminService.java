package com.furfaro.cyberguard.services.admin;

import com.furfaro.cyberguard.models.signal.ScenarioSignal;
import com.furfaro.cyberguard.models.signal.Signal;
import com.furfaro.cyberguard.repository.sim.ScenarioRepository;
import com.furfaro.cyberguard.repository.signal.ScenarioSignalRepository;
import com.furfaro.cyberguard.repository.signal.SignalRepository;
import com.furfaro.cyberguard.services.signal.dto.ScenarioSignalDTO;
import com.furfaro.cyberguard.services.signal.dto.SignalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignalAdminService {

    private final SignalRepository signalRepository;
    private final ScenarioSignalRepository scenarioSignalRepository;
    private final ScenarioRepository scenarioRepository;

    // -------- Catálogo de señales --------

    @Transactional
    public Signal upsertSignal(SignalDTO dto) {
        if (dto.getCategoria() == null) throw new IllegalArgumentException("categoria requerida");

        if (dto.getId() == null) {
            Signal s = Signal.builder()
                    .categoria(dto.getCategoria())
                    .descripcion(dto.getDescripcion())
                    .build();
            return signalRepository.save(s);
        } else {
            Signal s = signalRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Señal no encontrada: " + dto.getId()));
            if (dto.getCategoria() != null) s.setCategoria(dto.getCategoria());
            if (dto.getDescripcion() != null) s.setDescripcion(dto.getDescripcion());
            return signalRepository.save(s);
        }
    }

    // -------- Gabarito por escenario --------

    @Transactional
    public void attachSignalToScenario(Long scenarioId, Long signalId, int peso, boolean obligatoria) {
        scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new IllegalArgumentException("Escenario no encontrado: " + scenarioId));
        Signal s = signalRepository.findById(signalId)
                .orElseThrow(() -> new IllegalArgumentException("Señal no encontrada: " + signalId));

        boolean exists = scenarioSignalRepository.existsByEscenarioIdAndSenialId(scenarioId, signalId);
        if (!exists) {
            ScenarioSignal link = ScenarioSignal.builder()
                    .escenario(scenarioRepository.getReferenceById(scenarioId))
                    .senial(s)
                    .peso(peso)
                    .obligatoria(obligatoria)
                    .build();
            scenarioSignalRepository.save(link);
        } else {
            // si ya existe, permitimos actualizar peso/obligatoria
            var link = scenarioSignalRepository.findByEscenarioId(scenarioId).stream()
                    .filter(ss -> ss.getSenial().getId().equals(signalId))
                    .findFirst()
                    .orElseThrow(); // no debería pasar
            link.setPeso(peso);
            link.setObligatoria(obligatoria);
            scenarioSignalRepository.save(link);
        }
    }

    @Transactional
    public void detachSignalFromScenario(Long scenarioId, Long signalId) {
        scenarioSignalRepository.deleteByEscenarioIdAndSenialId(scenarioId, signalId);
    }

    @Transactional(readOnly = true)
    public List<ScenarioSignalDTO> listScenarioSignals(Long scenarioId) {
        return scenarioSignalRepository.findByEscenarioId(scenarioId).stream()
                .map(ss -> ScenarioSignalDTO.builder()
                        .scenarioId(scenarioId)
                        .signalId(ss.getSenial().getId())
                        .peso(ss.getPeso())
                        .obligatoria(ss.isObligatoria())
                        .categoria(ss.getSenial().getCategoria().name())
                        .descripcion(ss.getSenial().getDescripcion())
                        .build())
                .toList();
    }
}