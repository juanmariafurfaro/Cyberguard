package com.furfaro.cyberguard.services.common.dto;

import com.furfaro.cyberguard.models.content.ResourceType;
import lombok.Data;

@Data
public class ResourceCreateDTO {
    private ResourceType tipo; // IMAGEN | AUDIO | VIDEO | HTML
    private String url;        // o key del storage
    private String transcript; // requerido para audio/video (accesibilidad)
    private String altText;    // requerido para imagen (accesibilidad)
}