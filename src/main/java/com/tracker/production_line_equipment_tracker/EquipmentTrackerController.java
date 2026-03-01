package com.tracker.production_line_equipment_tracker;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentTrackerController
{
    private final EquipmentTrackerService service;

    public EquipmentTrackerController(EquipmentTrackerService service) {
        this.service = service;
    }

    /**
     * GET /api/equipment
     * List all equipment, with optional query params for filtering.
     */
    @GetMapping
    public List<EquipmentDTO> getAll(
            @RequestParam(required = false) EquipmentStatus status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String name) {
        return null;
    }

    /**
     * GET /api/equipment/{id}
     * Retrieve single equipment by ID.
     */
    @GetMapping("/{id}")
    public EquipmentDTO getById(@PathVariable Long id) {
        return null;
    }

    /**
     * POST /api/equipment
     * Create new equipment record.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentDTO create( @RequestBody EquipmentDTO equipment) {
        return null;
    }

    /**
     * PUT /api/equipment/{id}
     * Full update of equipment.
     */
    @PutMapping("/{id}")
    public EquipmentDTO update(@PathVariable Long id, @RequestBody EquipmentDTO equipment) {
        return null;
    }

    /**
     * PATCH /api/equipment/{id}/status
     * Update only the status (common in production monitoring).
     */
    @PatchMapping("/{id}/status")
    public EquipmentDTO updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
            return null;
    }

    /**
     * DELETE /api/equipment/{id}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

    }

    /**
     * GET /api/equipment/maintenance-due
     * Quick report endpoint for maintenance candidates.
     */
    @GetMapping("/maintenance-due")
    public List<EquipmentDTO> getMaintenanceDue() {
        return null;
    }
}
