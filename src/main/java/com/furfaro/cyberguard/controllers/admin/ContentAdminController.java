package com.furfaro.cyberguard.controllers.admin;

import com.furfaro.cyberguard.models.course.Course;
import com.furfaro.cyberguard.models.course.Module;
import com.furfaro.cyberguard.models.sim.Scenario;
import com.furfaro.cyberguard.models.sim.Simulation;
import com.furfaro.cyberguard.services.admin.ContentAdminService;
import com.furfaro.cyberguard.services.admin.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class ContentAdminController {
    private final ContentAdminService service;

    @PostMapping("/course")
    public Course upsertCourse(@RequestBody CourseUpsertDTO dto) { return service.upsertCourse(dto); }

    @PostMapping("/course/{id}/toggle")
    public void toggleCourse(@PathVariable Long id, @RequestParam boolean active) { service.toggleCourse(id, active); }

    @PostMapping("/module")
    public Module upsertModule(@RequestBody ModuleUpsertDTO dto) { return service.upsertModule(dto); }

    @PostMapping("/simulation")
    public Simulation upsertSimulation(@RequestBody SimulationUpsertDTO dto) { return service.upsertSimulation(dto); }

    @PostMapping("/scenario")
    public Scenario upsertScenario(@RequestBody ScenarioUpsertDTO dto) { return service.upsertScenario(dto); }

    @PostMapping("/scenario/{scenarioId}/resource/{resourceId}")
    public void attachResource(@PathVariable Long scenarioId, @PathVariable Long resourceId) {
        service.attachResourceToScenario(scenarioId, resourceId);
    }

    @DeleteMapping("/scenario/{scenarioId}/resource/{resourceId}")
    public void detachResource(@PathVariable Long scenarioId, @PathVariable Long resourceId) {
        service.detachResourceFromScenario(scenarioId, resourceId);
    }
}