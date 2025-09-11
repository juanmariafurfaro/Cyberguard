package com.furfaro.cyberguard.services.run;

import com.furfaro.cyberguard.models.run.Attempt;
import com.furfaro.cyberguard.models.run.Evaluation;
import com.furfaro.cyberguard.models.run.AttemptState;
import com.furfaro.cyberguard.models.signal.ScenarioSignal;
import com.furfaro.cyberguard.models.signal.Signal;
import com.furfaro.cyberguard.repository.run.*;
import com.furfaro.cyberguard.repository.signal.*;
import com.furfaro.cyberguard.services.run.dto.EvaluationDTO;
import com.furfaro.cyberguard.services.run.dto.MissingSignalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final ScenarioSignalRepository scenarioSignalRepository;
    private final UserSignalResponseRepository userSignalResponseRepository;
    private final EvaluationRepository evaluationRepository;
    private final AttemptRepository attemptRepository;

    /**
     * Compara respuestas del usuario vs señales esperadas del escenario y
     * genera la evaluación. No expone el “gabarito” completo, solo números + feedback.
     */
    @Transactional
    public EvaluationDTO evaluateAttempt(Long attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Intento no encontrado: " + attemptId));

        if (attempt.getEstado() != AttemptState.EN_PROGRESO) {
            // si ya está cerrado y evaluado devolvemos lo persistido
            return evaluationRepository.findByIntentoId(attemptId)
                    .map(this::toDto)
                    .orElseThrow(() -> new IllegalStateException("Intento cerrado sin evaluación previa"));
        }

        // Señales esperadas del escenario (gabarito)
        List<ScenarioSignal> expectedLinks = scenarioSignalRepository.findByEscenarioId(attempt.getEscenario().getId());
        Map<Long, ScenarioSignal> expectedById = expectedLinks.stream()
                .collect(Collectors.toMap(ss -> ss.getSenial().getId(), ss -> ss));

        int maxScore = expectedLinks.stream().mapToInt(ScenarioSignal::getPeso).sum();

        // Respuestas del usuario en este intento
        var responses = userSignalResponseRepository.findByIntentoId(attemptId);

        // Aciertos: señales esperadas que el usuario marcó como detectadas=true
        int aciertos = 0;
        int puntaje = 0;

        // IDs de señales esperadas detectadas
        Set<Long> detectedExpected = new HashSet<>();

        for (var r : responses) {
            Long sid = r.getSenial().getId();
            if (Boolean.TRUE.equals(r.isDetectada()) && expectedById.containsKey(sid)) {
                detectedExpected.add(sid);
                aciertos++;
                puntaje += expectedById.get(sid).getPeso();
            }
        }

        // Omisiones: esperadas no detectadas
        List<MissingSignalDTO> missing = expectedLinks.stream()
                .filter(ss -> !detectedExpected.contains(ss.getSenial().getId()))
                .map(ss -> toMissing(ss.getSenial(), ss.getPeso(), ss.isObligatoria()))
                .toList();

        int omisiones = missing.size();

        // Feedback formativo (texto simple; podés reemplazar por una plantilla)
        String feedback = buildFeedbackText(missing, aciertos, omisiones);

        // Persistir Evaluation (1–1 con Attempt)
        Evaluation evaluation = Evaluation.builder()
                .intento(attempt)
                .puntaje(puntaje)
                .aciertos(aciertos)
                .omisiones(omisiones)
                .feedback(feedback)
                .fecha(Instant.now())
                .build();
        evaluationRepository.save(evaluation);

        // Cerrar intento
        attempt.setEstado(AttemptState.FINALIZADO);
        attempt.setFin(Instant.now());
        attemptRepository.save(attempt);

        return EvaluationDTO.builder()
                .attemptId(attempt.getId())
                .puntaje(puntaje)
                .maxPuntaje(maxScore)
                .aciertos(aciertos)
                .omisiones(omisiones)
                .feedback(feedback)
                .build();
    }

    private MissingSignalDTO toMissing(Signal s, int peso, boolean obligatoria) {
        return MissingSignalDTO.builder()
                .signalId(s.getId())
                .categoria(s.getCategoria().name())
                .descripcion(s.getDescripcion())
                .obligatoria(obligatoria)
                .peso(peso)
                .build();
    }

    private EvaluationDTO toDto(Evaluation e) {
        return EvaluationDTO.builder()
                .attemptId(e.getIntento().getId())
                .puntaje(e.getPuntaje())
                .maxPuntaje(e.getPuntaje() + e.getOmisiones()) // aproximación si no guardaste max; opcional guardar max
                .aciertos(e.getAciertos())
                .omisiones(e.getOmisiones())
                .feedback(e.getFeedback())
                .build();
    }

    private String buildFeedbackText(List<MissingSignalDTO> missing, int aciertos, int omisiones) {
        if (omisiones == 0) {
            return "¡Excelente! Detectaste todas las señales esperadas.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Aciertos: ").append(aciertos).append(". Omisiones: ").append(omisiones).append(". ");
        sb.append("Señales que faltó identificar:\n");
        for (MissingSignalDTO m : missing) {
            sb.append("- ").append(m.getCategoria())
              .append(" (").append(m.isObligatoria() ? "obligatoria" : "opcional").append("): ")
              .append(Optional.ofNullable(m.getDescripcion()).orElse("Revisar detalle")).append("\n");
        }
        sb.append("Consejo: revisá remitente real, URL al pasar el mouse, tono de urgencia y adjuntos sospechosos.");
        return sb.toString();
    }
}