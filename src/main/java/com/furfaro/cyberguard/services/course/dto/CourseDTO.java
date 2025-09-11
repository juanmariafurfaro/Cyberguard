package com.furfaro.cyberguard.services.course.dto;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public class CourseDTO {
    Long id;
    String nombre;
    String descripcion;
    boolean active;
}