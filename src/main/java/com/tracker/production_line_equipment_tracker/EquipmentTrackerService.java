package com.tracker.production_line_equipment_tracker;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentTrackerService
{
    private final EquipmentTrackerRepository repo;

    public EquipmentTrackerService(EquipmentTrackerRepository etr)
    {
        this.repo = etr;
    }

    /**
     * Retrieve all equipment records.
     */
    public List<EquipmentDTO> getAllEquipment(){return null;}

    /**
     * Retrieve a single equipment by its ID.
     * Throws exception if not found.
     */
    public EquipmentDTO getEquipmentById(Long id){return null;}

    /**
     * Create a new equipment record.
     */
    public EquipmentDTO createEquipment(EquipmentDTO equipment){return null;}

    /**
     * Update an existing equipment record (full update).
     */
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO equipment){return null;}

    /**
     * Partially update the status of equipment (common operation in factory).
     * May also accept a reason/comment for the change.
     */
    public EquipmentDTO updateStatus(Long id, EquipmentStatus newStatus, String changeReason){return null;}

    /**
     * Delete equipment record (soft delete or hard – decide later).
     */
    public void deleteEquipment(Long id){}

    /**
     * Get all equipment that appears to need maintenance soon
     * (based on uptime or last maintenance date).
     */
    public List<EquipmentDTO> getMaintenanceDue(){return null;}

    /**
     * Filter equipment by current status.
     */
    public List<EquipmentDTO> getEquipmentByStatus(EquipmentStatus status){return null;}

    private Equipment toEntity(EquipmentDTO dto) {
        Equipment entity = new Equipment();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setLocation(dto.getLocation());
        entity.setStatus(dto.getStatus());
        entity.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        entity.setOperationalHours(dto.getOperationalHours());
        // id, createdAt, updatedAt ignored on create/update
        return entity;
    }

    private EquipmentDTO toDto(Equipment entity) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setLocation(entity.getLocation());
        dto.setStatus(entity.getStatus());
        dto.setLastMaintenanceDate(entity.getLastMaintenanceDate());
        dto.setOperationalHours(entity.getOperationalHours());
        return dto;
    }
}
