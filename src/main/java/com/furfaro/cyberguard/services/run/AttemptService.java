package com.furfaro.cyberguard.services.run;

import com.furfaro.cyberguard.models.run.*;
import com.furfaro.cyberguard.models.sim.Scenario;
import com.furfaro.cyberguard.repository.run.*;
import com.furfaro.cyberguard.repository.sim.ScenarioRepository;
import com.furfaro.cyberguard.repository.signal.SignalRepository;
import com.furfaro.cyberguard.repository.user.UserRepository;
import com.furfaro.cyberguard.services.run.dto.InteractionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final ScenarioRepository scenarioRepository;
    private final InteractionRepository interactionRepository;
    private final UserSignalResponseRepository userSignalResponseRepository;
    private final UserRepository userRepository;
    private final SignalRepository signalRepository;   // ✅ nuevo

    @Transactional
    public Attempt startAttempt(Long userId, Long scenarioId) {
        Scenario scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new IllegalArgumentException("Escenario no encontrado: " + scenarioId));

        Attempt attempt = Attempt.builder()
                .usuario(userRepository.getReferenceById(userId)) // ✅ proxy
                .escenario(scenario)
                .inicio(Instant.now())
                .estado(AttemptState.EN_PROGRESO)
                .build();

        return attemptRepository.save(attempt);
    }

    @Transactional
    public void recordInteraction(Long attemptId, InteractionDTO dto) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Intento no encontrado: " + attemptId));

        if (attempt.getEstado() != AttemptState.EN_PROGRESO) {
            throw new IllegalStateException("El intento no está en progreso");
        }

        Interaction interaction = Interaction.builder()
                .intento(attempt)
                .tipo(dto.getTipo())
                .payloadJson(dto.getPayloadJson())
                .timestamp(Instant.now())
                .build();

        interactionRepository.save(interaction);
    }

    @Transactional
    public void upsertSignalResponse(Long attemptId, Long signalId, boolean detectada, String comentario) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Intento no encontrado: " + attemptId));

        if (attempt.getEstado() != AttemptState.EN_PROGRESO) {
            throw new IllegalStateException("El intento no está en progreso");
        }

        userSignalResponseRepository.findByIntentoId(attemptId).stream()
                .filter(r -> r.getSenial().getId().equals(signalId))
                .findFirst()
                .ifPresentOrElse(existing -> {
                    existing.setDetectada(detectada);
                    existing.setComentario(comentario);
                    userSignalResponseRepository.save(existing);
                }, () -> {
                    UserSignalResponse created = UserSignalResponse.builder()
                            .intento(attempt)
                            .senial(signalRepository.getReferenceById(signalId)) // ✅
                            .detectada(detectada)
                            .comentario(comentario)
                            .build();
                    userSignalResponseRepository.save(created);
                });
    }

    @Transactional
    public Attempt closeAttempt(Long attemptId) {
        return closeAttempt(attemptId, AttemptState.FINALIZADO);
    }

    @Transactional
    public Attempt closeAttempt(Long attemptId, AttemptState newState) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Intento no encontrado: " + attemptId));

        if (attempt.getEstado() != AttemptState.EN_PROGRESO) {
            return attempt; // idempotente
        }
        attempt.setFin(Instant.now());
        attempt.setEstado(newState);
        return attemptRepository.save(attempt);
    }
}
