package com.example.demo.repositories;


import com.example.demo.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculeRepository
    extends JpaRepository<Vehicule, Long> {

    Vehicule findByImmatriculation(String matricule);

}
