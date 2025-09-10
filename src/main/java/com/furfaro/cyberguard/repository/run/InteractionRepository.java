package com.furfaro.cyberguard.repository.run;

import com.furfaro.cyberguard.models.run.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByIntentoIdOrderByTimestampAsc(Long intentoId);
}