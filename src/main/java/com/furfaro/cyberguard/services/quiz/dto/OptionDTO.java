package com.furfaro.cyberguard.services.quiz.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class OptionDTO {
    Long id;
    String texto;
}


