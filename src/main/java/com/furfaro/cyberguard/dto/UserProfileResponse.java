package com.furfaro.cyberguard.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private List<CourseProgressDto> enrolledCourses;
    private List<CertificateDto> certificates;
}