package com.furfaro.cyberguard.services.user.dto;

import lombok.Data;

@Data
public class RegistroDTO {
    private String nombre;
    private String email;
    private String password;
}