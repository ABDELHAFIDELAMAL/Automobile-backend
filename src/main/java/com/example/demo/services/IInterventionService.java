package com.example.demo.services;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mechanic;
import com.example.demo.enums.Priority;
import com.example.demo.enums.Status;
import com.example.demo.enums.InterventionType;

import java.util.List;

public interface IInterventionService {
    List<Intervention> getAllInterventions();
    Intervention getInterventionById(Long id);
    Intervention createIntervention(Intervention intervention);
    Intervention updateIntervention(Long id, Intervention intervention);
    Intervention assignMechanic(Long id, Mechanic mechanic);
    Intervention setEstimatedCost(Long id, Double cost);
    Intervention addDiagnostic(Long id, String diagnostic);
    Intervention changeStatus(Long id, Status newStatus, String author);
    Intervention complete(Long id);
    Intervention returnIntervention(Long id);
    Intervention returnVehicle(Long id);
    List<Intervention> getInterventionsByMechanic(Long mechanicId);
    List<Intervention> getInterventionsByVehicle(Long vehicleId);
    List<Intervention> getDelayedInterventions();
    List<Intervention> getOverdueInterventions();
    Double calculateTotalCost();
    List<Intervention> getInterventionsByType(InterventionType type);
    List<Intervention> getInterventionsByPriority(Priority priority);
}
