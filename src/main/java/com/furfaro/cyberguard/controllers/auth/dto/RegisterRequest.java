package com.furfaro.cyberguard.controllers.auth.dto;
import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
}