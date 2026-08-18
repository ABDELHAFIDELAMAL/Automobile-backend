package com.example.demo.services.vehicule;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.entities.Status;
import com.example.demo.entities.Vehicule;
import com.example.demo.repositories.MecanicienRepository;
import com.example.demo.repositories.VehiculeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VehiculeService implements IVehiculeService {


    private final VehiculeRepository vehiculeRepository;
    private final MecanicienRepository mecanicienRepository;

    public VehiculeService(VehiculeRepository vehiculeRepository, MecanicienRepository mecanicienRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.mecanicienRepository = mecanicienRepository;
    }

    @Override
    public Vehicule createVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    public void deleteVehicule(Long id) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicule not found with id : " + id));

        vehiculeRepository.delete(vehicule);
    }

    @Override
    public Vehicule updateVehicule(Long id, Vehicule vehicule) {

        Vehicule vehicule1 = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicule n'existe pas avec id : " + id));

        vehicule1.setMarque(vehicule.getMarque());
        vehicule1.setModele(vehicule.getModele());
        vehicule1.setAnnee(vehicule.getAnnee());
        vehicule1.setKilometrage(vehicule.getKilometrage());
        vehicule1.setImmatriculation(vehicule.getImmatriculation());
        vehicule1.setClientFictif(vehicule.isClientFictif());

        return vehiculeRepository.save(vehicule1);
    }

    @Override
    public Vehicule getVehiculeById(Long id) {

        return vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicule n'existe pas avec id : " + id));
    }

    @Override
    public Vehicule getVehiculeByMatricule(String matricule) {
        return vehiculeRepository.findByImmatriculation(matricule);
    }

    @Override
    public Vehicule restituerVehicule(Long id, String username, String userRole) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicule not found with id : " + id));

        boolean existeInterventionNonRestituee =
                vehicule.getInterventions()
                        .stream()
                        .anyMatch(i ->
                                i.getStatus() != Status.RESTITUEE);

        if (existeInterventionNonRestituee) {
            throw new IllegalStateException(
                    "Certaines interventions ne sont pas encore restituées");
        }

        return vehicule;
    }

    @Override
    public Vehicule afecterMecanicien(Long idVehicule, Long idMecanicien) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicule not found with id : "
                                        + idVehicule));
        Mecanicien mecanicien = mecanicienRepository.findById(idMecanicien)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mecanicien not found with id : "
                                        + idMecanicien));

        if (!mecanicien.isDisponible()) {
            throw new IllegalStateException(
                    "Le mécanicien n'est pas disponible");
        }

        for (Intervention intervention : vehicule.getInterventions()) {
            if (intervention.getStatus() == Status.DEVIS_A_VALIDER) {
                intervention.setMecanicien(mecanicien);
                mecanicien.setDisponible(false);
                mecanicienRepository.save(mecanicien);
                break;
            }
        }
        return vehicule;
    }
    @Override
    public List<Vehicule> getVehiculeByStatus(Status status) {
        List<Vehicule> result = new ArrayList<>();
        for (Vehicule vehicule : vehiculeRepository.findAll()) {
            for (Intervention intervention : vehicule.getInterventions()) {
                if (intervention.getStatus() == status) {
                    result.add(vehicule);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public List<Vehicule> rechercher(String text) {
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        List<Vehicule> resultat = new ArrayList<>();
        for (Vehicule vehicule : vehicules) {
            if (vehicule.getMarque().contains(text)
                    || vehicule.getModele().contains(text)
                    || vehicule.getImmatriculation().contains(text)) {
                resultat.add(vehicule);
            }
        }
        return resultat;
    }
}