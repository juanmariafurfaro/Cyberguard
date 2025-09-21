package com.furfaro.cyberguard.services;


import com.furfaro.cyberguard.dto.CertificateDto;
import com.furfaro.cyberguard.dto.CourseProgressDto;
import com.furfaro.cyberguard.dto.UserProfileResponse;
import com.furfaro.cyberguard.exception.ResourceNotFoundException;
import com.furfaro.cyberguard.models.User;
import com.furfaro.cyberguard.models.UserCertificate;
import com.furfaro.cyberguard.models.UserProgress;
import com.furfaro.cyberguard.repository.UserCertificateRepository;
import com.furfaro.cyberguard.repository.UserProgressRepository;
import com.furfaro.cyberguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserCertificateRepository userCertificateRepository;

    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserProgress> userProgress = userProgressRepository.findByUserId(user.getId());
        List<UserCertificate> certificates = userCertificateRepository.findByUserId(user.getId());

        List<CourseProgressDto> courseProgressList = userProgress.stream()
                .map(progress -> CourseProgressDto.builder()
                        .courseId(progress.getCourse().getId())
                        .courseTitle(progress.getCourse().getTitle())
                        .courseDescription(progress.getCourse().getDescription())
                        .currentModule(progress.getCurrentModule())
                        .completionPercentage(progress.getCompletionPercentage())
                        .isCompleted(progress.getIsCompleted())
                        .finalScore(progress.getFinalScore())
                        .build())
                .collect(Collectors.toList());

        List<CertificateDto> certificateList = certificates.stream()
                .map(cert -> CertificateDto.builder()
                        .id(cert.getId())
                        .courseTitle(cert.getCourse().getTitle())
                        .certificateCode(cert.getCertificateCode())
                        .issuedDate(cert.getIssuedDate())
                        .finalScore(cert.getFinalScore())
                        .build())
                .collect(Collectors.toList());

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .enrolledCourses(courseProgressList)
                .certificates(certificateList)
                .build();
    }
}