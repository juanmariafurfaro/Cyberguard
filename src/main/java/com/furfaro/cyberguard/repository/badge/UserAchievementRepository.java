package com.furfaro.cyberguard.repository.badge;

import com.furfaro.cyberguard.models.badge.UserAchievement;
import com.furfaro.cyberguard.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndLogroId(Long usuarioId, Long logroId);
    Optional<UserAchievement> findByUsuarioIdAndLogroId(Long usuarioId, Long logroId);
}