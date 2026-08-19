package com.example.demo.controllers;


import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.entities.Status;
import com.example.demo.entities.TypeIntervention;
import com.example.demo.services.intervention.IInterventionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/interventions")
public class InterventionController  {
    private final IInterventionService interventionService;

    public InterventionController(IInterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @GetMapping
    public List<Intervention> getAllInterventions() {
        return interventionService.getAllInterventions();
    }

    @PostMapping(path = "/create")
    public Intervention createIntervention(@RequestBody Intervention intervention) {
        return interventionService.createIntervention(intervention);
    }

    @PutMapping(path = "/update/{id}")
    public Intervention updateIntervnetion(@PathVariable Long id,@RequestBody Intervention intervention) {
        return interventionService.updateIntervnetion(id , intervention);
    }

    @PatchMapping(path = "assign/{id}")
    public Intervention assignMecanicien(@PathVariable Long id,@RequestBody Mecanicien mecanicien) {
        return interventionService.assignMecanicien(id , mecanicien);
    }

    @PostMapping(path = "/setcout/{id}")
    public Intervention setCoutEstime(@PathVariable Long id,@RequestParam Double cout) {
        return interventionService.setCoutEstime(id , cout);
    }

    @PostMapping(path = "/ajouter/diagnostic/{id}")
    public Intervention addDiagnostic(@PathVariable Long id,@RequestParam String diagnostic) {
        return interventionService.addDiagnostic(id , diagnostic);
    }

    @PatchMapping(path = "/change/status/{id}")
    public Intervention changerStatus(@PathVariable Long id,@RequestBody Status statusIntervention) {
        return interventionService.changerStatus(id , statusIntervention);
    }

    @PatchMapping(path = "/terminer/{id}")
    public Intervention terminer(@PathVariable Long id) {
        return interventionService.terminer(id);
    }

    @PatchMapping(path = "/restituer/{id}")
    public Intervention restituer(@PathVariable Long id) {
        return interventionService.restituer(id);
    }

    @GetMapping(path = "/by/mecanicien/{id}")
    public List<Intervention> getInterventionByMecanicien(@PathVariable Long id) {
        return interventionService.getInterventionByMecanicien(id);
    }

    @GetMapping(path = "/by/vehicule/{id}")
    public List<Intervention> getInterventionByVehicule(@PathVariable Long id) {
        return interventionService.getInterventionByVehicule(id);
    }

    @GetMapping(path = "/en/retard")
    public List<Intervention> getEnRetard() {
        return interventionService.getEnRetard();
    }

    @GetMapping(path = "/calculer/cout/total/{id}")
    public Double calculerCoutTotal(@PathVariable Long id) {
        return interventionService.calculerCoutTotal(id);
    }

    @GetMapping(path = "/by/type/{id}")
    public List<Intervention> getInterventionsByType(@RequestBody TypeIntervention type) {
        return interventionService.getInterventionsByType(type);
    }
}
