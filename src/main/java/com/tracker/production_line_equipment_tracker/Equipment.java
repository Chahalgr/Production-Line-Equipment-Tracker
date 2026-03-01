package com.tracker.production_line_equipment_tracker;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "equipments")
public class Equipment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String type;
    private String location;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    private LocalDate lastMaintenanceDate;
    private int operationalHours;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOperationalHours() {
        return operationalHours;
    }

    public void setOperationalHours(int operationalHours) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//    public Equipment(String name, String type, String location, String status, LocalDate date, int operationalHours)
//    {
//        this.name = name;
//        this.type = type;
//        this.location = location;
//        this.status = status;
//        this.lastMaintenanceDate = date;
//        this.operationalHours = operationalHours;
//    }
//
//    public Equipment() {
//
//    }
}
