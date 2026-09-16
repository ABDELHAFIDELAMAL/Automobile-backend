package com.example.demo.repositories;

import com.example.demo.entities.Mecanicien;
import com.example.demo.enums.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MecanicienRepository
    extends JpaRepository<Mecanicien , Long> {
    List<Mecanicien> findMecanicienByDisponible(boolean disponible);

    boolean existsByNom(String nom);

    List<Mecanicien> findMecanicienBySpecialite(Specialite specialite);
}
