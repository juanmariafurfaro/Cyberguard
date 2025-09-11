package com.furfaro.cyberguard.services.admin.dto;

import lombok.Data;

@Data
public class CourseUpsertDTO {
    private Long id;            // null = crear, not null = update
    private String nombre;
    private String descripcion;
    private Boolean active;     // opcional; default true al crear
}
