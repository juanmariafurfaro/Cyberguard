package com.furfaro.cyberguard.controllers.auth;

import com.furfaro.cyberguard.controllers.auth.dto.LoginRequest;
import com.furfaro.cyberguard.controllers.auth.dto.RegisterRequest;
import com.furfaro.cyberguard.controllers.auth.dto.TokenResponse;
import com.furfaro.cyberguard.services.user.UserService;
import com.furfaro.cyberguard.services.user.dto.CredencialesDTO;
import com.furfaro.cyberguard.services.user.dto.RegistroDTO;
import com.furfaro.cyberguard.services.user.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest req) {
        RegistroDTO dto = new RegistroDTO();
        dto.setNombre(req.getNombre());
        dto.setEmail(req.getEmail());
        dto.setPassword(req.getPassword());
        userService.register(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest req) {
        CredencialesDTO dto = new CredencialesDTO();
        dto.setEmail(req.getEmail());
        dto.setPassword(req.getPassword());

        TokenDTO tokenDTO = userService.login(dto);     // el service ya genera el token
        return ResponseEntity.ok(new TokenResponse(tokenDTO.getToken()));
    }
}
