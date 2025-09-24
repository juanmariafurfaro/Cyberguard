package com.furfaro.cyberguard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @JsonProperty("email")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    
    @JsonProperty("firstName")
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @JsonProperty("lastName")
    @NotBlank(message = "Last name is required")
    private String lastName;
}