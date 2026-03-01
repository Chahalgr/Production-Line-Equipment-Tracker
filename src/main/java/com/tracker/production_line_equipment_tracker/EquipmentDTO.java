package com.tracker.production_line_equipment_tracker;
import java.time.LocalDate;

/**
 * Single DTO used for both requests and responses in the Equipment Tracker API.
 *
 * - For POST (create) and PUT/PATCH (update): Client sends most fields; id is ignored/generated.
 * - For GET responses: Server returns all fields, including id and timestamps.
 *
 * This simplifies mapping while still decoupling the API from the JPA entity.
 */
public class EquipmentDTO {

    private Long id;
    private String name;
    private String type;
    private String location;
    private EquipmentStatus status;
    private LocalDate lastMaintenanceDate;
    private Integer operationalHours;

    public Integer getOperationalHours() {
        return operationalHours;
    }

    public void setOperationalHours(Integer operationalHours) {
        this.operationalHours = operationalHours;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public EquipmentDTO() {}

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
