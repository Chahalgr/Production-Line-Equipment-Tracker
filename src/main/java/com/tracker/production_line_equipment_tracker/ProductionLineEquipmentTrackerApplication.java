package com.tracker.production_line_equipment_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller("/")
public class ProductionLineEquipmentTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductionLineEquipmentTrackerApplication.class, args);
    }

    @RequestMapping("/")
    public String index()
    {
        return "index.html";
    }


}
