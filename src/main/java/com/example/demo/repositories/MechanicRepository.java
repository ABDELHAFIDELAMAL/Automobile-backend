package com.example.demo.repositories;

import com.example.demo.entities.Mechanic;
import com.example.demo.enums.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MechanicRepository
        extends JpaRepository<Mechanic, Long> {
    List<Mechanic> findMechanicByAvailable(boolean available);

    boolean existsByName(String name);

    List<Mechanic> findMechanicBySpecialty(Specialty specialty);
}
