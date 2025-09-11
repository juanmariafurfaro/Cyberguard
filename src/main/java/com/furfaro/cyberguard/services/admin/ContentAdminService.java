package com.furfaro.cyberguard.services.admin;


import com.furfaro.cyberguard.models.content.Resource;
import com.furfaro.cyberguard.models.content.ScenarioResource;
import com.furfaro.cyberguard.models.course.Course;
import com.furfaro.cyberguard.models.course.Module;
import com.furfaro.cyberguard.models.course.ModuleType;
import com.furfaro.cyberguard.models.sim.Scenario;
import com.furfaro.cyberguard.models.sim.Simulation;
import com.furfaro.cyberguard.repository.course.*;
import com.furfaro.cyberguard.repository.sim.ScenarioRepository;
import com.furfaro.cyberguard.repository.sim.SimulationRepository;
import com.furfaro.cyberguard.repository.content.ResourceRepository;
import com.furfaro.cyberguard.repository.content.ScenarioResourceRepository;
import com.furfaro.cyberguard.services.admin.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentAdminService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final SimulationRepository simulationRepository;
    private final ScenarioRepository scenarioRepository;
    private final ResourceRepository resourceRepository;
    private final ScenarioResourceRepository scenarioResourceRepository;

    // -------- Courses --------

    @Transactional
    public Course upsertCourse(CourseUpsertDTO dto) {
        if (dto.getId() == null) {
            Course c = Course.builder()
                    .nombre(dto.getNombre())
                    .descripcion(dto.getDescripcion())
                    .isActive(dto.getActive() == null ? true : dto.getActive())
                    .build();
            return courseRepository.save(c);
        } else {
            Course c = courseRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + dto.getId()));
            if (dto.getNombre() != null) c.setNombre(dto.getNombre());
            if (dto.getDescripcion() != null) c.setDescripcion(dto.getDescripcion());
            if (dto.getActive() != null) c.setActive(dto.getActive());
            return courseRepository.save(c);
        }
    }

    @Transactional
    public void toggleCourse(Long id, boolean active) {
        Course c = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + id));
        c.setActive(active);
        courseRepository.save(c);
    }

    // -------- Modules --------

    @Transactional
    public Module upsertModule(ModuleUpsertDTO dto) {
        if (dto.getCursoId() == null) throw new IllegalArgumentException("cursoId requerido");
        if (dto.getOrden() == null) throw new IllegalArgumentException("orden requerido");
        if (dto.getTipo() == null) throw new IllegalArgumentException("tipo requerido");

        Course curso = courseRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + dto.getCursoId()));

        if (dto.getId() == null) {
            Module m = Module.builder()
                    .curso(curso)
                    .nombre(dto.getNombre())
                    .tipo(dto.getTipo())
                    .orden(dto.getOrden())
                    .isActive(dto.getActive() == null ? true : dto.getActive())
                    .build();
            return moduleRepository.save(m);
        } else {
            Module m = moduleRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado: " + dto.getId()));
            m.setCurso(curso);
            if (dto.getNombre() != null) m.setNombre(dto.getNombre());
            if (dto.getTipo() != null) m.setTipo(dto.getTipo());
            if (dto.getOrden() != null) m.setOrden(dto.getOrden());
            if (dto.getActive() != null) m.setActive(dto.getActive());
            return moduleRepository.save(m);
        }
    }

    // -------- Simulations --------

    @Transactional
    public Simulation upsertSimulation(SimulationUpsertDTO dto) {
        if (dto.getModuloId() == null) throw new IllegalArgumentException("moduloId requerido");
        if (dto.getOrden() == null) throw new IllegalArgumentException("orden requerido");
        if (dto.getTipo() == null) throw new IllegalArgumentException("tipo requerido");

        Module modulo = moduleRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado: " + dto.getModuloId()));
        if (modulo.getTipo() != ModuleType.SIMULACION) {
            throw new IllegalStateException("La simulación debe pertenecer a un módulo de tipo SIMULACION");
        }

        if (dto.getId() == null) {
            Simulation s = Simulation.builder()
                    .modulo(modulo)
                    .nombre(dto.getNombre())
                    .descripcion(dto.getDescripcion())
                    .tipo(dto.getTipo())
                    .orden(dto.getOrden())
                    .isActive(dto.getActive() == null ? true : dto.getActive())
                    .build();
            return simulationRepository.save(s);
        } else {
            Simulation s = simulationRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Simulación no encontrada: " + dto.getId()));
            s.setModulo(modulo);
            if (dto.getNombre() != null) s.setNombre(dto.getNombre());
            if (dto.getDescripcion() != null) s.setDescripcion(dto.getDescripcion());
            if (dto.getTipo() != null) s.setTipo(dto.getTipo());
            if (dto.getOrden() != null) s.setOrden(dto.getOrden());
            if (dto.getActive() != null) s.setActive(dto.getActive());
            return simulationRepository.save(s);
        }
    }

    // -------- Scenarios --------

    @Transactional
    public Scenario upsertScenario(ScenarioUpsertDTO dto) {
        if (dto.getSimulacionId() == null) throw new IllegalArgumentException("simulacionId requerido");
        if (dto.getOrden() == null) throw new IllegalArgumentException("orden requerido");
        if (dto.getDificultad() == null) throw new IllegalArgumentException("dificultad requerida");

        Simulation sim = simulationRepository.findById(dto.getSimulacionId())
                .orElseThrow(() -> new IllegalArgumentException("Simulación no encontrada: " + dto.getSimulacionId()));

        if (dto.getId() == null) {
            Scenario e = Scenario.builder()
                    .simulacion(sim)
                    .nombre(dto.getNombre())
                    .descripcion(dto.getDescripcion())
                    .orden(dto.getOrden())
                    .dificultad(dto.getDificultad())
                    .isActive(dto.getActive() == null ? true : dto.getActive())
                    .build();
            return scenarioRepository.save(e);
        } else {
            Scenario e = scenarioRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Escenario no encontrado: " + dto.getId()));
            e.setSimulacion(sim);
            if (dto.getNombre() != null) e.setNombre(dto.getNombre());
            if (dto.getDescripcion() != null) e.setDescripcion(dto.getDescripcion());
            if (dto.getOrden() != null) e.setOrden(dto.getOrden());
            if (dto.getDificultad() != null) e.setDificultad(dto.getDificultad());
            if (dto.getActive() != null) e.setActive(dto.getActive());
            return scenarioRepository.save(e);
        }
    }

    // -------- Attach/Detach Resource ↔ Scenario --------

    @Transactional
    public void attachResourceToScenario(Long scenarioId, Long resourceId) {
        Scenario scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new IllegalArgumentException("Escenario no encontrado: " + scenarioId));
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + resourceId));

        boolean exists = scenarioResourceRepository.existsByEscenarioIdAndRecursoId(scenario.getId(), resource.getId());
        if (!exists) {
            ScenarioResource link = ScenarioResource.builder()
                    .escenario(scenario)
                    .recurso(resource)
                    .build();
            scenarioResourceRepository.save(link);
        }
    }

    @Transactional
    public void detachResourceFromScenario(Long scenarioId, Long resourceId) {
        // borrado idempotente
        scenarioResourceRepository.deleteByEscenarioIdAndRecursoId(scenarioId, resourceId);
    }
}