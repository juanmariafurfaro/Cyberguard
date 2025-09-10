package com.furfaro.cyberguard.repository.signal;

import com.furfaro.cyberguard.models.signal.Signal;
import com.furfaro.cyberguard.models.signal.SignalCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignalRepository extends JpaRepository<Signal, Long> {
    List<Signal> findByCategoria(SignalCategory categoria);
}