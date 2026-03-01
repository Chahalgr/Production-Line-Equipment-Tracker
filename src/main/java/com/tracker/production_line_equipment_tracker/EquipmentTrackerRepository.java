package com.tracker.production_line_equipment_tracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentTrackerRepository extends JpaRepository<Equipment, Long> {
}
