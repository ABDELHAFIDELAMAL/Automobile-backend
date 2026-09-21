package com.example.demo.services;

import com.example.demo.entities.Vehicle;
import com.example.demo.enums.Status;

import java.util.List;

public interface IVehicleService {
    Vehicle createVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    void deleteVehicle(Long id);
    Vehicle updateVehicle(Long id, Vehicle vehicle);
    Vehicle getVehicleById(Long id);
    Vehicle getVehicleByMatricule(String matricule);
    Vehicle returnVehicle(Long id, String username, String userRole);
    List<Vehicle> getVehicleByStatus(Status status);
    List<Vehicle> search(String text);
}
