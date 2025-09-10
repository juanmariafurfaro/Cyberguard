package com.furfaro.cyberguard.services.user;

import com.furfaro.cyberguard.models.user.User;
import com.furfaro.cyberguard.repository.user.UserRepository;
import com.furfaro.cyberguard.services.user.dto.CredencialesDTO;
import com.furfaro.cyberguard.services.user.dto.RegistroDTO;
import com.furfaro.cyberguard.services.user.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider; // clase util que te muestro abajo

    public User register(RegistroDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = User.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .contraseniaHash(passwordEncoder.encode(dto.getPassword()))
                .fechaRegistro(LocalDate.now())
                .build();

        return userRepository.save(user);
    }

    public TokenDTO login(CredencialesDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getContraseniaHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtProvider.generateToken(user.getEmail());
        return new TokenDTO(token);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}