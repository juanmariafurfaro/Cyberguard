package com.furfaro.cyberguard.services.quiz;


import com.furfaro.cyberguard.models.quiz.Option;
import com.furfaro.cyberguard.models.quiz.Question;
import com.furfaro.cyberguard.models.quiz.QuizAnswer;
import com.furfaro.cyberguard.repository.course.ModuleRepository;
import com.furfaro.cyberguard.repository.quiz.OptionRepository;
import com.furfaro.cyberguard.repository.quiz.QuestionRepository;
import com.furfaro.cyberguard.repository.quiz.QuizAnswerRepository;
import com.furfaro.cyberguard.repository.user.UserRepository;   // ✅ IMPORTANTE
import com.furfaro.cyberguard.services.quiz.dto.OptionDTO;
import com.furfaro.cyberguard.services.quiz.dto.QuestionDTO;
import com.furfaro.cyberguard.services.quiz.dto.QuizResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final QuizAnswerRepository quizAnswerRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;              // ✅ inyectado

    // ---------- Mappers ----------
    private QuestionDTO toDto(Question q, List<Option> options) {
        return QuestionDTO.builder()
                .id(q.getId())
                .moduloId(q.getModulo().getId())
                .enunciado(q.getEnunciado())
                .opciones(options.stream()
                        .sorted(Comparator.comparing(Option::getId))
                        .map(o -> OptionDTO.builder()
                                .id(o.getId())
                                .texto(o.getTexto())
                                .build())
                        .toList())
                .build();
    }

    // ---------- API ----------

    /** Devuelve el quiz del módulo (ordenado). NO incluye cuál opción es correcta. */
    public List<QuestionDTO> getQuiz(Long moduleId) {
        moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado: " + moduleId));

        var questions = questionRepository.findByModuloIdOrderByOrdenAsc(moduleId);
        return questions.stream()
                .map(q -> {
                    var options = optionRepository.findByPreguntaId(q.getId());
                    return toDto(q, options);
                })
                .toList();
    }

    /**
     * Guarda respuesta del usuario y devuelve si fue correcta.
     * Controles mínimos:
     *  - valida que la opción pertenezca a la pregunta
     *  - upsert si ya había respondido esa pregunta
     */
    @Transactional
    public QuizResultDTO answer(Long userId, Long questionId, Long optionId) {
        Question q = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + questionId));

        Option opt = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Opción no encontrada: " + optionId));

        if (!opt.getPregunta().getId().equals(q.getId())) {
            throw new IllegalArgumentException("La opción no pertenece a la pregunta");
        }

        boolean correcta = Boolean.TRUE.equals(opt.isEsCorrecta());

        var prev = quizAnswerRepository.findByUsuarioId(userId).stream()
                .filter(a -> a.getPregunta().getId().equals(questionId))
                .findFirst();

        QuizAnswer answer = prev.orElseGet(() -> QuizAnswer.builder()
                .usuario(userRepository.getReferenceById(userId))   // ✅ proxy por id (sin SELECT)
                .pregunta(q)
                .opcion(opt)
                .correcta(correcta)
                .fecha(Instant.now())
                .build());

        if (prev.isPresent()) {
            answer.setOpcion(opt);
            answer.setCorrecta(correcta);
            answer.setFecha(Instant.now());
        }

        quizAnswerRepository.save(answer);

        return QuizResultDTO.builder()
                .questionId(q.getId())
                .selectedOptionId(opt.getId())
                .correcta(correcta)
                .build();
    }
}