package com.furfaro.cyberguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.furfaro.cyberguard.models.usuario;

public interface usuarioRepository extends JpaRepository<usuario, Long> {}
