package com.furfaro.cyberguard.controllers;


import com.furfaro.cyberguard.dto.CourseDetailDto;
import com.furfaro.cyberguard.dto.CourseProgressDto;
import com.furfaro.cyberguard.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseProgressDto>> getAllCourses(Authentication authentication) {
        String email = authentication.getName();
        List<CourseProgressDto> courses = courseService.getAllCoursesForUser(email);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CourseDetailDto> getCourseDetail(@PathVariable Long courseId, Authentication authentication) {
        String email = authentication.getName();
        CourseDetailDto course = courseService.getCourseDetail(courseId, email);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> enrollInCourse(@PathVariable Long courseId, Authentication authentication) {
        String email = authentication.getName();
        courseService.enrollUserInCourse(courseId, email);
        return ResponseEntity.ok(new MessageResponse("Successfully enrolled in course"));
    }
}
