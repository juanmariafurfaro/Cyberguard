package com.furfaro.cyberguard.services.common;

import com.furfaro.cyberguard.models.content.Resource;
import com.furfaro.cyberguard.models.content.ResourceType;
import com.furfaro.cyberguard.repository.content.ResourceRepository;
import com.furfaro.cyberguard.services.common.dto.ResourceCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final ResourceRepository resourceRepository;

    @Transactional
    public Resource createResource(ResourceCreateDTO dto) {
        validateAccessibility(dto);
        // si usás S3/GCS, acá subirías el archivo y obtendrías la URL/Key
        Resource r = Resource.builder()
                .tipo(dto.getTipo())
                .url(dto.getUrl())
                .transcript(dto.getTranscript())
                .altText(dto.getAltText())
                .build();
        return resourceRepository.save(r);
    }

    // En un futuro: generar URL firmada para subida/descarga segura
    public String signUrl(String rawUrlOrKey) {
        // placeholder: retornar tal cual; reemplazar por firma real si usás S3/GCS
        return rawUrlOrKey;
    }

    private void validateAccessibility(ResourceCreateDTO dto) {
        if (dto.getTipo() == ResourceType.IMAGEN && (dto.getAltText() == null || dto.getAltText().isBlank())) {
            throw new IllegalArgumentException("altText requerido para imágenes");
        }
        if ((dto.getTipo() == ResourceType.AUDIO || dto.getTipo() == ResourceType.VIDEO)
                && (dto.getTranscript() == null || dto.getTranscript().isBlank())) {
            throw new IllegalArgumentException("transcript requerido para audio/video");
        }
        if (dto.getUrl() == null || dto.getUrl().isBlank()) {
            throw new IllegalArgumentException("url requerida");
        }
    }
}