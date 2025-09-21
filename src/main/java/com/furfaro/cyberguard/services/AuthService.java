package com.furfaro.cyberguard.services;


import com.furfaro.cyberguard.dto.AuthResponse;
import com.furfaro.cyberguard.dto.LoginRequest;
import com.furfaro.cyberguard.dto.RegisterRequest;
import com.furfaro.cyberguard.exception.BadRequestException;
import com.furfaro.cyberguard.exception.UnauthorizedException;
import com.furfaro.cyberguard.models.User;
import com.furfaro.cyberguard.repository.UserRepository;
import com.furfaro.cyberguard.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 30; // 30 minutes

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email is already taken!");
        }

        // Validar fortaleza de contraseña
        validatePasswordStrength(registerRequest.getPassword());

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.Role.USER);

        User savedUser = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return AuthResponse.builder()
                .token(jwt)
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .role(savedUser.getRole().name())
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // Verificar si la cuenta está bloqueada
        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account is locked due to multiple failed login attempts. Try again later.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Reset failed attempts on successful login
            if (user.getFailedAttempts() > 0) {
                userRepository.unlockAccount(user.getEmail());
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            return AuthResponse.builder()
                    .token(jwt)
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().name())
                    .build();

        } catch (BadCredentialsException e) {
            // Increment failed attempts
            increaseFailedAttempts(user);
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    private void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempts() + 1;
        userRepository.updateFailAttempts(newFailAttempts, user.getEmail());

        if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
            userRepository.lockAccount(LocalDateTime.now(), user.getEmail());
        }
    }

    private void validatePasswordStrength(String password) {
        if (password.length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            throw new BadRequestException("Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*[a-z].*")) {
            throw new BadRequestException("Password must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            throw new BadRequestException("Password must contain at least one number");
        }
        
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new BadRequestException("Password must contain at least one special character");
        }
    }
}