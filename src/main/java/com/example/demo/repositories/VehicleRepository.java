package com.example.demo.repositories;


import com.example.demo.entities.Vehicle;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository
    extends JpaRepository<Vehicle, Long> {
    boolean existsByMatricule(@NotNull String licensePlate);
    Vehicle findByMatricule(String licensePlate);
}
