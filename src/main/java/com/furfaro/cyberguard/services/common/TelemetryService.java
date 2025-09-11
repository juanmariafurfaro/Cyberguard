package com.furfaro.cyberguard.services.common;

import com.furfaro.cyberguard.models.run.Attempt;
import com.furfaro.cyberguard.models.run.AttemptState;
import com.furfaro.cyberguard.services.common.dto.OmittedSignalStatDTO;
import com.furfaro.cyberguard.services.common.dto.QuestionErrorStatDTO;
import com.furfaro.cyberguard.services.common.dto.ScenarioStatsDTO;
import com.furfaro.cyberguard.repository.run.AttemptRepository;
import com.furfaro.cyberguard.repository.signal.ScenarioSignalRepository;
import com.furfaro.cyberguard.repository.run.UserSignalResponseRepository;
import com.furfaro.cyberguard.repository.quiz.QuizAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TelemetryService {

    private final AttemptRepository attemptRepository;
    private final UserSignalResponseRepository userSignalResponseRepository;
    private final ScenarioSignalRepository scenarioSignalRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    public ScenarioStatsDTO scenarioStats(Long scenarioId) {
        var attempts = attemptRepository.findByEscenarioId(scenarioId);
        long total = attempts.size();
        long completed = attempts.stream().filter(a -> a.getEstado() == AttemptState.FINALIZADO).count();
        double completionRate = total == 0 ? 0 : (double) completed / total;

        double avgSeconds = attempts.stream()
                .filter(a -> a.getFin() != null)
                .mapToLong(a -> Duration.between(a.getInicio(), a.getFin()).toSeconds())
                .average().orElse(0);

        return ScenarioStatsDTO.builder()
                .scenarioId(scenarioId)
                .attempts(total)
                .completed(completed)
                .completionRate(completionRate)
                .avgDurationSec(avgSeconds)
                .build();
    }

    /** Top señales más omitidas (no detectadas) en este escenario. */
    public List<OmittedSignalStatDTO> topOmittedSignals(Long scenarioId) {
        var expected = scenarioSignalRepository.findByEscenarioId(scenarioId)
                .stream().map(ss -> ss.getSenial().getId()).collect(Collectors.toSet());

        var byAttempt = attemptRepository.findByEscenarioId(scenarioId)
                .stream().map(Attempt::getId).toList();

        Map<Long, Long> contador = new HashMap<>(); // signalId -> omisiones
        for (Long attemptId : byAttempt) {
            var responses = userSignalResponseRepository.findByIntentoId(attemptId);
            Set<Long> detected = responses.stream()
                    .filter(r -> Boolean.TRUE.equals(r.isDetectada()))
                    .map(r -> r.getSenial().getId())
                    .collect(Collectors.toSet());
            for (Long sid : expected) {
                if (!detected.contains(sid)) {
                    contador.merge(sid, 1L, Long::sum);
                }
            }
        }

        return contador.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(e -> OmittedSignalStatDTO.builder()
                        .signalId(e.getKey())
                        .omisiones(e.getValue())
                        .build())
                .toList();
    }

    /** Preguntas con mayor tasa de error (para mejorar contenidos). */
    public List<QuestionErrorStatDTO> hardestQuestionsByModule(Long moduleId) {
        var answers = quizAnswerRepository.findAll().stream()
                .filter(a -> a.getPregunta().getModulo().getId().equals(moduleId))
                .toList();

        Map<Long, long[]> acc = new HashMap<>(); // questionId -> [total, errores]
        for (var a : answers) {
            long[] arr = acc.computeIfAbsent(a.getPregunta().getId(), k -> new long[2]);
            arr[0] += 1;                 // total
            if (!a.isCorrecta()) arr[1] += 1; // errores
        }

        return acc.entrySet().stream()
                .map(e -> {
                    long total = e.getValue()[0];
                    long err = e.getValue()[1];
                    double rate = total == 0 ? 0 : (double) err / total;
                    return QuestionErrorStatDTO.builder()
                            .questionId(e.getKey())
                            .respuestas(total)
                            .errores(err)
                            .wrongRate(rate)
                            .build();
                })
                .sorted(Comparator.comparing(QuestionErrorStatDTO::getWrongRate).reversed())
                .toList();
    }
}
