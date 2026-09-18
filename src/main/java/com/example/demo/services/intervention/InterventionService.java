package com.example.demo.services.intervention;

import com.example.demo.entities.*;
import com.example.demo.enums.Status;
import com.example.demo.enums.TypeIntervention;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.repositories.HistoriqueRepository;
import com.example.demo.repositories.InterventionRepository;
import com.example.demo.repositories.MecanicienRepository;
import com.example.demo.repositories.VehiculeRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.example.demo.enums.Status.*;

@Service
@Transactional
public class InterventionService implements IInterventionService {

        private final InterventionRepository interventionRepository;
        private final MecanicienRepository mecanicienRepository;
        private final HistoriqueRepository historiqueRepository;
        private final VehiculeRepository vehiculeRepository;

        public InterventionService(
                InterventionRepository interventionRepository,
                MecanicienRepository mecanicienRepository,
                HistoriqueRepository historiqueRepository,
                VehiculeRepository vehiculeRepository) {

            this.interventionRepository = interventionRepository;
            this.mecanicienRepository = mecanicienRepository;
            this.historiqueRepository = historiqueRepository;
            this.vehiculeRepository = vehiculeRepository;
        }

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public Intervention getInterventionById(Long id) {
        return interventionRepository.findById(id).orElseThrow(()->new RuntimeException("Intervention not found id " + id));
    }

    @Override
    public Intervention createIntervention(Intervention intervention) {
        boolean exist = interventionRepository.existsByVehiculeIdAndTypeAndDescription(intervention.getVehicule().getId(), intervention.getType(), intervention.getDescription());

        if(exist){
            throw new AllReadyExistException("Intervention already exists with id " + intervention.getId());
        }else{
            return interventionRepository.save(intervention);
        }

    }

    @Override
    public Intervention updateIntervention(Long id, Intervention intervention) {

        Intervention interv = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        interv.setDiagnostic(intervention.getDiagnostic());
        interv.setType(intervention.getType());
        interv.setDescription(intervention.getDescription());
        interv.setStatus(intervention.getStatus());
        interv.setDateCloture(intervention.getDateCloture());
        interv.setMecanicien(intervention.getMecanicien());
        interv.setPriorite(intervention.getPriorite());
        interv.setDateDepot(intervention.getDateDepot());
        interv.setDateRestitutionPrevue(intervention.getDateRestitutionPrevue());
        interv.setCoutEstime(intervention.getCoutEstime());

        return interventionRepository.save(interv);
    }

    @Override
    public Intervention assignMecanicien(Long id, Mecanicien mecanicien) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        Mecanicien mecanicien1 = mecanicienRepository.findById(mecanicien.getId())
                .orElseThrow(() ->
                        new RuntimeException("Mecanicien not found with id : "
                                + mecanicien.getId()));

        if (!mecanicien1.isDisponible()) {
            throw new IllegalStateException(
                    "Ce mécanicien n'est pas disponible actuellement.");
        }

        intervention.setMecanicien(mecanicien1);

        if (intervention.getStatus() == Status.DEVIS_A_VALIDER) {
            intervention.setStatus(EN_REPARATION);
        }

        mecanicien1.setDisponible(false);
        mecanicienRepository.save(mecanicien1);

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention setCoutEstime(Long id, Double cout) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (cout == null || cout <= 0) {
            throw new IllegalArgumentException(
                    "Le coût estimé doit être supérieur à 0");
        }

        intervention.setCoutEstime(cout);

        if (intervention.getStatus() == DIAGNOSTIC_EN_COURS) {
            intervention.setStatus(Status.DEVIS_A_VALIDER);
        }

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention addDiagnostic(Long id, String diagnostic) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (intervention.getStatus() != RECUE) {
            throw new IllegalStateException(
                    "Impossible d'ajouter un diagnostic");
        }

        Status ancienStatus = intervention.getStatus();
        Status nouveauStatus = DIAGNOSTIC_EN_COURS;

        intervention.setDiagnostic(diagnostic);
        intervention.setStatus(nouveauStatus);

        Intervention saved = interventionRepository.save(intervention);

        HistoriqueIntervention historique = new HistoriqueIntervention();
        historique.setIntervention(saved);
        historique.setAncienStatus(ancienStatus);
        historique.setNouveauStatus(nouveauStatus);
        historique.setCommentaire("Ajout du diagnostic technique par le mécanicien.");
        historique.setAuteur(
                saved.getMecanicien() != null
                        ? saved.getMecanicien().getNom()
                        : "SYSTEM");
        historique.setDate(LocalDateTime.now());

        historiqueRepository.save(historique);

        return saved;
    }

    @Override
    public Intervention changerStatus(Long id, Status nouveauStatus, String auteur ) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention introuvable"));

        Status statusActual = intervention.getStatus() ;

        boolean valide = switch (statusActual) {
            case RECUE -> nouveauStatus == DIAGNOSTIC_EN_COURS;
            case DIAGNOSTIC_EN_COURS -> nouveauStatus == Status.DEVIS_A_VALIDER;
            case DEVIS_A_VALIDER -> nouveauStatus == EN_REPARATION;
            case EN_REPARATION -> nouveauStatus == Status.TERMINEE;
            case TERMINEE -> nouveauStatus == Status.RESTITUEE;
            case RESTITUEE -> false;
            default -> false;
        };

        if ("TERMINEE".equals(intervention.getStatus())) {
            mecanicienRepository.findById(intervention.getMecanicien().getId())
                    .ifPresent(mecanicien -> {
                        mecanicien.setDisponible(true);
                        mecanicienRepository.save(mecanicien);
                    });
        }


        if (!valide) {
            throw new IllegalStateException("Transition de " + statusActual + " vers " + nouveauStatus + " interdite.");
        }
        HistoriqueIntervention history = new HistoriqueIntervention();
        history.setIntervention(intervention);
        history.setAncienStatus(statusActual);
        history.setNouveauStatus(nouveauStatus);
        history.setDate(LocalDateTime.now());
        history.setAuteur(auteur);
        historiqueRepository.save(history);

        intervention.setStatus(nouveauStatus);


        return interventionRepository.save(intervention);
    }


    @Override
    public Intervention terminer(Long id) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (intervention.getStatus() != EN_REPARATION) {
            throw new IllegalStateException(
                    "L'intervention doit être EN_REPARATION");
        }

        Status ancienStatus = intervention.getStatus();

        intervention.setStatus(Status.TERMINEE);
        intervention.setDateCloture(LocalDate.now().atStartOfDay());

        interventionRepository.save(intervention);

        HistoriqueIntervention historique = new HistoriqueIntervention();
        historique.setIntervention(intervention);
        historique.setAncienStatus(ancienStatus);
        historique.setNouveauStatus(Status.TERMINEE);
        historique.setCommentaire("Intervention terminée");
        historique.setAuteur(
                intervention.getMecanicien() != null
                        ? intervention.getMecanicien().getNom()
                        : "SYSTEM");
        historique.setDate(LocalDateTime.now());

        historiqueRepository.save(historique);

        return intervention;
    }

    @Override
    public Intervention restituer(Long id) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (intervention.getStatus() != Status.TERMINEE) {
            throw new IllegalStateException(
                    "L'intervention doit être TERMINÉE avant restitution");
        }

        Status ancienStatus = intervention.getStatus();

        intervention.setStatus(Status.RESTITUEE);

        if (intervention.getMecanicien() != null) {
            Mecanicien mecanicien = intervention.getMecanicien();
            mecanicien.setDisponible(true);
            mecanicienRepository.save(mecanicien);
        }

        interventionRepository.save(intervention);

        HistoriqueIntervention historique = new HistoriqueIntervention();
        historique.setIntervention(intervention);
        historique.setAncienStatus(ancienStatus);
        historique.setNouveauStatus(Status.RESTITUEE);
        historique.setCommentaire("Véhicule restitué au client");
        historique.setAuteur(
                intervention.getMecanicien() != null
                        ? intervention.getMecanicien().getNom()
                        : "SYSTEM");
        historique.setDate(LocalDateTime.now());

        historiqueRepository.save(historique);

        return intervention;
    }

    @Override
    public List<Intervention> getInterventionByMecanicien(Long idMecanicien) {

        Mecanicien mecanicien = mecanicienRepository.findById(idMecanicien)
                .orElseThrow(() ->
                        new RuntimeException("Mecanicien not found : "
                                + idMecanicien));

        return mecanicien.getInterventions();
    }

    @Override
    public List<Intervention> getInterventionByVehicule(Long idVehicule) {

        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() ->
                        new RuntimeException("Vehicule not found : "
                                + idVehicule));

        return vehicule.getInterventions();
    }

    @Override
    public List<Intervention> getEnRetard() {

        return interventionRepository
                .findByDateRestitutionPrevueBeforeAndStatusNot(
                        LocalDate.now().atStartOfDay(),
                        Status.RESTITUEE
                );
    }

    @Override
    public Double calculerCoutTotal() {

        List<Intervention> interventions = interventionRepository.findAll();

        double somme = 0;

        for (Intervention intervention : interventions) {

            if (intervention.getCoutEstime() != null) {
                somme += intervention.getCoutEstime();
            }
        }

        return somme;
    }

    @Override
    public List<Intervention> getInterventionsByType(TypeIntervention type) {
        return interventionRepository.findInterventionByType(type);
    }


}