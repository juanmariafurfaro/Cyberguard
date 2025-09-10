package com.furfaro.cyberguard.repository.badge;

import com.furfaro.cyberguard.models.badge.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByNombreIgnoreCase(String nombre);
}
