package com.furfaro.cyberguard.repository.quiz;

import com.furfaro.cyberguard.models.quiz.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByModuloIdOrderByOrdenAsc(Long moduloId);
}