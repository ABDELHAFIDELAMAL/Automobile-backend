package com.example.demo.services.mecanicien;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.repositories.InterventionRepository;
import com.example.demo.repositories.MecanicienRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MecanicienService implements IMecanicienService {

    private final MecanicienRepository mecanicienRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    public MecanicienService(MecanicienRepository mecanicienRepository) {
        this.mecanicienRepository = mecanicienRepository;
    }

    @Override
    public List<Mecanicien> getAllMecaniciens() {
        return mecanicienRepository.findAll();
    }

    @Override
    public Mecanicien getMecanicienById(Long id) {
        return mecanicienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mecanicien not found with id : " + id));
    }

    @Override
    public List<Mecanicien> getMecaniciensDisponibles(boolean disponible) {
        return mecanicienRepository.findMecanicienByDisponible(disponible);
    }

    @Override
    public Mecanicien createMecanicien(Mecanicien mecanicien) {
        return mecanicienRepository.save(mecanicien);
    }

    @Override
    public Mecanicien updateMecanicien(Long id, Mecanicien mecanicien) {
        Mecanicien mecanicienExistant = mecanicienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mecanicien not found with id : " + id));

        mecanicienExistant.setNom(mecanicien.getNom());
        mecanicienExistant.setDisponible(mecanicien.isDisponible());
        mecanicienExistant.setSpecialite(mecanicien.getSpecialite());

        return mecanicienRepository.save(mecanicienExistant);
    }

    @Override
    public void deleteMecanicien(Long id) {
        Mecanicien mecanicien = mecanicienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mecanicien not found with id : " + id));

        mecanicienRepository.delete(mecanicien);
    }

    @Override
    public Mecanicien activer(Long id) {
        Mecanicien mecanicien = mecanicienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mecanicien n'existe pas avec l'id = " + id));

        if (mecanicien.isDisponible()) {
            throw new IllegalStateException(
                    "Mecanicien est déjà activé");
        }

        mecanicien.setDisponible(true);

        return mecanicienRepository.save(mecanicien);
    }

    @Override
    public Mecanicien desactiver(Long id) {
        Mecanicien mecanicien = mecanicienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mecanicien n'existe pas avec l'id = " + id));

        if (!mecanicien.isDisponible()) {
            throw new IllegalStateException(
                    "Mecanicien est déjà désactivé");
        }

        mecanicien.setDisponible(false);

        return mecanicienRepository.save(mecanicien);
    }

    @Override
    public List<Intervention> getInterventions(Long id) {
        Mecanicien mecanicien = mecanicienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mecanicien n'existe pas avec l'id = " + id));

        return interventionRepository.findInterventionsByMecanicien(mecanicien);
    }

    @Override
    public Map<Long, Integer> getCharge() {
        List<Mecanicien> mecaniciens = mecanicienRepository.findAll();

        Map<Long, Integer> charges = new HashMap<>();

        for (Mecanicien mecanicien : mecaniciens) {
            charges.put(
                    mecanicien.getId(),
                    mecanicien.getInterventions().size()
            );
        }

        return charges;
    }
}