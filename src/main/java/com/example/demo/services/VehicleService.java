package com.example.demo.services;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Vehicle;
import com.example.demo.enums.Status;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.repositories.MechanicRepository;
import com.example.demo.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;

    public VehicleService(VehicleRepository vehicleRepository, MechanicRepository mechanicRepository) {
        this.vehicleRepository = vehicleRepository;
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByMatricule(vehicle.getMatricule())) {
            throw new AllReadyExistException("Vehicle already exists with this license plate: " + vehicle.getMatricule());
        } else {
            return vehicleRepository.save(vehicle);
        }
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        vehicleRepository.delete(vehicle);
    }

    @Override
    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle does not exist with id: " + id));

        existingVehicle.setMake(vehicle.getMake());
        existingVehicle.setModel(vehicle.getModel());
        existingVehicle.setYear(vehicle.getYear());
        existingVehicle.setMileage(vehicle.getMileage());
        existingVehicle.setMatricule(vehicle.getMatricule());
        existingVehicle.setDummyClient(vehicle.isDummyClient());

        return vehicleRepository.save(existingVehicle);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle does not exist with id: " + id));
    }

    @Override
    public Vehicle getVehicleByMatricule(String matricule) {
        return vehicleRepository.findByMatricule(matricule);
    }

    @Override
    public Vehicle returnVehicle(Long id, String username, String userRole) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));

        boolean hasUnreturnedInterventions = vehicle.getInterventions()
                .stream()
                .anyMatch(i -> i.getStatus() != Status.RETURNED);

        if (hasUnreturnedInterventions) {
            throw new IllegalStateException("Some interventions have not been returned yet");
        }

        return vehicle;
    }

    @Override
    public List<Vehicle> getVehicleByStatus(Status status) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : vehicleRepository.findAll()) {
            for (Intervention intervention : vehicle.getInterventions()) {
                if (intervention.getStatus() == status) {
                    result.add(vehicle);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public List<Vehicle> search(String text) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getMake().contains(text)
                    || vehicle.getModel().contains(text)
                    || vehicle.getMatricule().contains(text)) {
                result.add(vehicle);
            }
        }
        return result;
    }
}
