package com.furfaro.cyberguard.controllers;


import com.furfaro.cyberguard.dto.*;
import com.furfaro.cyberguard.services.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping("/{moduleId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ModuleDto> getModule(@PathVariable Long moduleId, Authentication authentication) {
        String email = authentication.getName();
        ModuleDto module = moduleService.getModuleForUser(moduleId, email);
        return ResponseEntity.ok(module);
    }

    @PostMapping("/{moduleId}/start")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> startModule(@PathVariable Long moduleId, Authentication authentication) {
        String email = authentication.getName();
        moduleService.startModule(moduleId, email);
        return ResponseEntity.ok(new MessageResponse("Module started successfully"));
    }

    @PutMapping("/{moduleId}/progress")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateModuleProgress(
            @PathVariable Long moduleId,
            @Valid @RequestBody ModuleProgressUpdateRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        moduleService.updateModuleProgress(moduleId, email, request);
        return ResponseEntity.ok(new MessageResponse("Progress updated successfully"));
    }

    @PostMapping("/{moduleId}/complete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> completeModule(@PathVariable Long moduleId, Authentication authentication) {
        String email = authentication.getName();
        moduleService.completeModule(moduleId, email);
        return ResponseEntity.ok(new MessageResponse("Module completed successfully"));
    }
}