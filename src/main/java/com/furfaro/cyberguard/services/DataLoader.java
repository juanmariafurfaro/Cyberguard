package com.furfaro.cyberguard.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.furfaro.cyberguard.models.usuario;
import com.furfaro.cyberguard.repository.usuarioRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final usuarioRepository repo;

    public DataLoader(usuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        usuario u = new usuario();
        u.setNombre("Test User");
        u.setEmail("test@correo.com");
        repo.save(u);

        System.out.println("✅ Usuario de prueba insertado en la BD");
    }
}
