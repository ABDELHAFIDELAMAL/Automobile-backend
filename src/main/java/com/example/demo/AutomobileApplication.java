package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.enums.*;
import com.example.demo.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class AutomobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomobileApplication.class, args);
        log.info(" Server running  ...!");
    }

    @Bean
    CommandLineRunner start(
                            IInterventionService interventionService,
                            IMechanicService mechanicService,
                            IVehicleService vehicleService,
                            IInterventionHistoryService interventionHistoryService) {
        return args -> {


        };
    }
}
