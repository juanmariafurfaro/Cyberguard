package com.furfaro.cyberguard.repository.quiz;

import com.furfaro.cyberguard.models.quiz.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByPreguntaId(Long preguntaId);
}