package com.furfaro.cyberguard.services.sim;

import com.furfaro.cyberguard.models.content.Resource;
import com.furfaro.cyberguard.models.content.ScenarioResource;
import com.furfaro.cyberguard.models.sim.Scenario;
import com.furfaro.cyberguard.repository.sim.ScenarioRepository;
import com.furfaro.cyberguard.repository.content.ScenarioResourceRepository;
import com.furfaro.cyberguard.services.sim.dto.ResourceViewDTO;
import com.furfaro.cyberguard.services.sim.dto.ScenarioViewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScenarioContentService {

    private final ScenarioRepository scenarioRepository;
    private final ScenarioResourceRepository scenarioResourceRepository;

    public ScenarioViewDTO getScenarioForPlay(Long scenarioId) {
        Scenario s = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new IllegalArgumentException("Escenario no encontrado: " + scenarioId));

        // Traer recursos asociados (imágenes, audio, video, html)
        List<ScenarioResource> links = scenarioResourceRepository.findByEscenarioId(s.getId());

        List<ResourceViewDTO> recursos = links.stream()
                .map(ScenarioResource::getRecurso)
                .sorted(Comparator.comparing(Resource::getId))
                .map(r -> ResourceViewDTO.builder()
                        .id(r.getId())
                        .tipo(r.getTipo())
                        .url(r.getUrl())
                        .transcript(r.getTranscript()) // accesibilidad
                        .altText(r.getAltText())       // accesibilidad
                        .build())
                .toList();

        // IMPORTANTE: no devolvemos señales esperadas ni gabarito
        return ScenarioViewDTO.builder()
                .id(s.getId())
                .simulacionId(s.getSimulacion().getId())
                .nombre(s.getNombre())
                .descripcion(s.getDescripcion())
                .orden(s.getOrden())
                .dificultad(s.getDificultad())
                .recursos(recursos)
                .build();
    }
}