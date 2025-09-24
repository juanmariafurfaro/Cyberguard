package com.furfaro.cyberguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @JsonProperty("email")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    private String password;
}