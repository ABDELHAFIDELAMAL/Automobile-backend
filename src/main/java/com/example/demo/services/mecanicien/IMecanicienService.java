package com.example.demo.services.mecanicien;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.enums.Specialite;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IMecanicienService {
    List<Mecanicien> getAllMecaniciens();
    Mecanicien getMecanicienById(Long id);
    List<Mecanicien> getMecaniciensDisponibles(boolean disponible);
    Mecanicien createMecanicien(Mecanicien mecanicien);
    Mecanicien updateMecanicien(Long id , Mecanicien mecanicien);
    void deleteMecanicien(Long id);
    Mecanicien activer(Long id);
    Mecanicien desactiver(Long id);
    List<Intervention> getInterventions(Long id);
    Map<Long , Integer> getCharge();
    List<Mecanicien> getMecaniciensBySpecialite(Specialite specialite);
}
