package com.furfaro.cyberguard.repository.quiz;

import com.furfaro.cyberguard.models.quiz.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long> {
    List<QuizAnswer> findByUsuarioId(Long usuarioId);
    List<QuizAnswer> findByPreguntaId(Long preguntaId);
}