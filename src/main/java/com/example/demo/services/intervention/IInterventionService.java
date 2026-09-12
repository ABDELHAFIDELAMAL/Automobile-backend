package com.example.demo.services.intervention;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.enums.Status;
import com.example.demo.enums.TypeIntervention;

import java.util.List;

public interface IInterventionService {
    List<Intervention> getAllInterventions();
    Intervention createIntervention(Intervention intervention);
    Intervention updateIntervnetion(Long id , Intervention intervention);
    Intervention assignMecanicien(Long id , Mecanicien mecanicien);
    Intervention setCoutEstime(Long id , Double cout);
    Intervention addDiagnostic(Long id, String diagnostic);
    Intervention changerStatus(Long id , Status statusIntervention);
    Intervention terminer(Long id);
    Intervention restituer(Long id);
    List<Intervention> getInterventionByMecanicien(Long idMecanicien);
    List<Intervention> getInterventionByVehicule(Long idVehicule);
    List<Intervention> getEnRetard();
    Double calculerCoutTotal(Long id);
    List<Intervention> getInterventionsByType(TypeIntervention type);
}
