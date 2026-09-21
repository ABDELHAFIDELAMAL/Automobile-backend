package com.example.demo.services;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mechanic;
import com.example.demo.enums.Specialty;
import java.util.List;
import java.util.Map;

public interface IMechanicService {
    List<Mechanic> getAllMechanics();
    Mechanic getMechanicById(Long id);
    List<Mechanic> getAvailableMechanics(boolean available);
    Mechanic createMechanic(Mechanic mechanic);
    Mechanic updateMechanic(Long id, Mechanic mechanic);
    void deleteMechanic(Long id);
    Mechanic activate(Long id);
    Mechanic deactivate(Long id);
    List<Intervention> getInterventions(Long id);
    Map<Long, Integer> getWorkload();
    List<Mechanic> getMechanicsBySpecialty(Specialty specialty);
}
