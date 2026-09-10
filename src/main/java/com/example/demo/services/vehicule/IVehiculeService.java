package com.example.demo.services.vehicule;

import com.example.demo.entities.Vehicule;
import com.example.demo.enums.Status;

import java.util.List;

public interface IVehiculeService {
    Vehicule createVehicule(Vehicule vehicule);
    List<Vehicule> getAllVehicules();
    void deleteVehicule(Long id);
    Vehicule updateVehicule(Long id , Vehicule vehicule);
    Vehicule getVehiculeById(Long id);
    Vehicule getVehiculeByMatricule(String matricule);
    Vehicule restituerVehicule(Long id, String username, String userRole);
    Vehicule afecterMecanicien(Long idVehicule ,Long idMecanicien );
    List<Vehicule> getVehiculeByStatus(Status status);
    List<Vehicule> rechercher(String text);
}
