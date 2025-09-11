package com.furfaro.cyberguard.services.sim.dto;

import com.furfaro.cyberguard.models.content.ResourceType;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ResourceViewDTO {
    Long id;
    ResourceType tipo; // IMAGEN | AUDIO | VIDEO | HTML
    String url;
    String transcript; // accesibilidad para audio/video
    String altText;    // accesibilidad para imágenes
}