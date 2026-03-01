package com.tracker.production_line_equipment_tracker;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class EquipmentTrackerService
{
    private final EquipmentTrackerRepository repo;
    private final int maxNoMaintenanceDays = 60;
    private final int maxOperationalHours = 1445;

    public EquipmentTrackerService(EquipmentTrackerRepository etr)
    {
        this.repo = etr;
    }

    /**
     * Retrieve all equipment records with optional filters
     */
    public List<EquipmentDTO> getAll(EquipmentStatus status, String location, String name)
    {
        return repo.findAll().stream()
                .filter(e -> status == null || e.getStatus() == status)
                .filter(e -> location == null || e.getLocation().equals(location))
                .filter(e -> name == null || e.getName().equals(name))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a single equipment by its ID.
     * Throws exception if not found.
     */
    public EquipmentDTO getEquipmentById(Long id)
    {
        return toDto(repo.findById(id).orElseThrow(() -> new NoSuchElementException("Equipment not found")));
    }

    /**
     * Create a new equipment record.
     */
    public EquipmentDTO createEquipment(EquipmentDTO equipment)
    {
        Equipment eq = this.toEntity(equipment);
        Equipment entered = repo.save(eq);
        return toDto(entered);
    }

    /**
     * Update an existing equipment record (full update).
     */
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO equipment)
    {
        Equipment eq = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Equipment doesn't exist"));
        eq.setName(equipment.getName());
        eq.setOperationalHours(equipment.getOperationalHours());
        eq.setStatus(equipment.getStatus());
        eq.setLocation(equipment.getLocation());
        eq.setLastMaintenanceDate(equipment.getLastMaintenanceDate());
        eq.setType(equipment.getType());
        return toDto(repo.save(eq));
    }

    /**
     * Partially update the status of equipment (common operation in factory).
     * May also accept a reason/comment for the change.
     */
    public EquipmentDTO updateStatus(Long id, EquipmentStatus newStatus)
    {
        Equipment eq = repo.findById(id).orElseThrow(() -> new NoSuchElementException("No such equipment"));
        eq.setStatus(newStatus);
        return toDto(repo.save(eq));
    }

    /**
     * Delete equipment record (soft delete or hard – decide later).
     */
    public void deleteEquipment(Long id)
    {
        if(!repo.existsById(id))
        {
            throw new NoSuchElementException("No such equipment exists");
        }
        repo.deleteById(id);
    }

    /**
     * Get all equipment that appears to need maintenance soon
     * (based on uptime or last maintenance date).
     */
    public List<EquipmentDTO> getMaintenanceDue()
    {
        List<Equipment> eq = repo.findAll();
        List<EquipmentDTO> list = new ArrayList<EquipmentDTO>();
        LocalDate cutoff = LocalDate.now().minusDays(maxNoMaintenanceDays);
        for(Equipment e: eq)
        {
            LocalDate lastDate = e.getLastMaintenanceDate();
            int opHours = e.getOperationalHours();
            if(lastDate.isBefore(cutoff)
                || opHours >= maxOperationalHours)
            {
                list.add(toDto(e));
            }
        }
        return list;
    }

    private Equipment toEntity(EquipmentDTO dto) {
        Equipment entity = new Equipment();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setLocation(dto.getLocation());
        entity.setStatus(dto.getStatus());
        entity.setLastMaintenanceDate(dto.getLastMaintenanceDate());
        entity.setOperationalHours(dto.getOperationalHours());
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
