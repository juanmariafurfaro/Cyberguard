package com.furfaro.cyberguard.repository.run;

import com.furfaro.cyberguard.models.run.UserSignalResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSignalResponseRepository extends JpaRepository<UserSignalResponse, Long> {
    List<UserSignalResponse> findByIntentoId(Long intentoId);
    boolean existsByIntentoIdAndSenialId(Long intentoId, Long senialId);
    void deleteByIntentoId(Long intentoId);
}