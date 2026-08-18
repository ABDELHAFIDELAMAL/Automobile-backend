package com.example.demo.repositories;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.entities.Status;
import com.example.demo.entities.TypeIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterventionRepository
    extends JpaRepository<Intervention , Long> {

    List<Intervention> findInterventionsByMecanicien(Mecanicien mecanicien);

    List<Intervention> findByDateRestitutionPrevueBeforeAndStatusNot(LocalDate now, Status status);

    List<Intervention> findInterventionByType(TypeIntervention type);
}
