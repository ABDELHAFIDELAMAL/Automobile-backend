package com.example.demo.services;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mechanic;
import com.example.demo.enums.Specialty;

import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.repositories.InterventionRepository;
import com.example.demo.repositories.MechanicRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MechanicService implements IMechanicService {

    private final MechanicRepository mechanicRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    public MechanicService(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public List<Mechanic> getAllMechanics() {
        return mechanicRepository.findAll();
    }

    @Override
    public Mechanic getMechanicById(Long id) {
        return mechanicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mechanic not found with id : " + id));
    }

    @Override
    public List<Mechanic> getAvailableMechanics(boolean available) {
        return mechanicRepository.findMechanicByAvailable(available);
    }

    @Override
    public Mechanic createMechanic(Mechanic mechanic) {
        if (mechanicRepository.existsByName(mechanic.getName())) {
            throw new AllReadyExistException("Mechanic already exists with name: " + mechanic.getName());
        } else {
            return mechanicRepository.save(mechanic);
        }
    }

    @Override
    public Mechanic updateMechanic(Long id, Mechanic mechanic) {
        Mechanic existingMechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mechanic not found with id : " + id));

        existingMechanic.setName(mechanic.getName());
        existingMechanic.setAvailable(mechanic.isAvailable());
        existingMechanic.setSpecialty(mechanic.getSpecialty());

        return mechanicRepository.save(existingMechanic);
    }

    @Override
    public void deleteMechanic(Long id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mechanic not found with id : " + id));

        mechanicRepository.delete(mechanic);
    }

    @Override
    public Mechanic activate(Long id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mechanic does not exist with id = " + id));

        if (mechanic.isAvailable()) {
            throw new IllegalStateException(
                    "Mechanic is already activated");
        }

        mechanic.setAvailable(true);

        return mechanicRepository.save(mechanic);
    }

    @Override
    public Mechanic deactivate(Long id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mechanic does not exist with id = " + id));

        if (!mechanic.isAvailable()) {
            throw new IllegalStateException(
                    "Mechanic is already deactivated");
        }

        mechanic.setAvailable(false);

        return mechanicRepository.save(mechanic);
    }

    @Override
    public List<Intervention> getInterventions(Long id) {
        Mechanic mechanic = mechanicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Mechanic does not exist with id = " + id));

        return interventionRepository.findInterventionsByMechanic(mechanic);
    }

    @Override
    public Map<Long, Integer> getWorkload() {
        List<Mechanic> mechanics = mechanicRepository.findAll();

        Map<Long, Integer> workloads = new HashMap<>();

        for (Mechanic mechanic : mechanics) {
            workloads.put(
                    mechanic.getId(),
                    mechanic.getInterventions().size()
            );
        }

        return workloads;
    }

    @Override
    public List<Mechanic> getMechanicsBySpecialty(Specialty specialty) {
        return mechanicRepository.findMechanicBySpecialty(specialty);
    }
}
