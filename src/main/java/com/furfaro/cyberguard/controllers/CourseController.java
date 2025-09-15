package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.services.course.CourseService;
import com.furfaro.cyberguard.services.course.dto.CourseDTO;
import com.furfaro.cyberguard.services.course.dto.ModuleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public List<CourseDTO> listActive() {
        return courseService.listActiveCourses();
    }

    @GetMapping("/{courseId}/modules")
    public List<ModuleDTO> modules(@PathVariable Long courseId) {
        return courseService.listModulesOfCourse(courseId);
    }

    @GetMapping("/modules/{moduleId}")
    public ModuleDTO module(@PathVariable Long moduleId) {
        return courseService.getModule(moduleId);
    }
}
