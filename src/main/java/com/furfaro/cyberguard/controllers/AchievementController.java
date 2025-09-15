package com.furfaro.cyberguard.controllers;

import com.furfaro.cyberguard.services.badge.AchievementService;
import com.furfaro.cyberguard.services.badge.dto.AchievementDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping("/user/{userId}")
    public List<AchievementDTO> listByUser(@PathVariable Long userId) {
        return achievementService.listByUser(userId);
    }
}