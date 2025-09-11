package com.furfaro.cyberguard.services.quiz.dto;


import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value @Builder
public class QuestionDTO {
    Long id;
    Long moduloId;
    String enunciado;

    @Singular("opcion")
    List<OptionDTO> opciones; // sin marcar cuál es correcta
}