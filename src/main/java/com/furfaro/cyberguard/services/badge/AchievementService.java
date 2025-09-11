package com.furfaro.cyberguard.services.badge;


import com.furfaro.cyberguard.models.badge.Achievement;
import com.furfaro.cyberguard.models.badge.UserAchievement;
import com.furfaro.cyberguard.repository.badge.AchievementRepository;
import com.furfaro.cyberguard.repository.badge.UserAchievementRepository;
import com.furfaro.cyberguard.services.badge.dto.AchievementDTO;
import com.furfaro.cyberguard.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserRepository userRepository; 

    /**
     * Otorga un logro al usuario si no lo tiene. Si el logro no existe en catálogo, lo crea.
     */
    @Transactional
    public void grantIfNotExists(Long userId, String nombreLogro) {
        Achievement ach = achievementRepository.findByNombreIgnoreCase(nombreLogro)
                .orElseGet(() -> achievementRepository.save(
                        Achievement.builder()
                                .nombre(nombreLogro)
                                .descripcion(null) // opcional: completar luego
                                .build()
                ));

        boolean yaLoTiene = userAchievementRepository.existsByUsuarioIdAndLogroId(userId, ach.getId());
        if (!yaLoTiene) {
            UserAchievement ua = UserAchievement.builder()
                    .usuario(userRepository.getReferenceById(userId))
                    .logro(ach)
                    .fechaObtencion(Instant.now())
                    .build();
            userAchievementRepository.save(ua);
        }
    }

    /** Lista los logros obtenidos por el usuario. */
    @Transactional(readOnly = true)
    public List<AchievementDTO> listByUser(Long userId) {
        var list = userAchievementRepository.findByUsuarioId(userId);
        return list.stream()
                .map(ua -> AchievementDTO.builder()
                        .id(ua.getLogro().getId())
                        .nombre(ua.getLogro().getNombre())
                        .descripcion(ua.getLogro().getDescripcion())
                        .fechaObtencion(ua.getFechaObtencion())
                        .build())
                .toList();
    }
}