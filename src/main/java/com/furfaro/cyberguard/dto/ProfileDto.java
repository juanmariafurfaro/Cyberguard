package com.furfaro.cyberguard.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDto {
    private String name;
    private String company;
    private String position;
    private String email;
    private String phone;
    private String location;
    private String additionalInfo;
}