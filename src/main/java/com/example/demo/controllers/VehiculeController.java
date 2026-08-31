package com.example.demo.controllers;

import com.example.demo.enums.Status;
import com.example.demo.entities.Vehicule;
import com.example.demo.services.vehicule.IVehiculeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicules")
@CrossOrigin(origins = "http://localhost:4200")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ USER')")
    public List<Vehicule> getAllVehicles() {
        return vehiculeService.getAllVehicules();
    }

    @PostMapping("/create")
    public Vehicule createVehicule(@RequestBody Vehicule vehicule) {
        return vehiculeService.createVehicule(vehicule);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehiculeService.deleteVehicule(id);
    }

    @PutMapping("/update/{id}")
    public Vehicule updateVehicule(@PathVariable Long id, @RequestBody Vehicule vehicule) {
        return vehiculeService.updateVehicule(id, vehicule);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Vehicule getVehicleById(@PathVariable Long id) {
        return vehiculeService.getVehiculeById(id);
    }

    @GetMapping("/by/matricule")
    public Vehicule getVehiculeByMatricule(@RequestParam String matricule) {
        return vehiculeService.getVehiculeByMatricule(matricule);
    }

    @PatchMapping("/restituer/{id}")
    public Vehicule restituerVehicule(@PathVariable Long id,
            @RequestParam String username, @RequestParam String userRole) {
        return vehiculeService.restituerVehicule(id, username, userRole);
    }

    @PatchMapping("/{idVehicle}/affecter/mecanicien/{idMechanic}")
    public Vehicule affecterMecanicien(@PathVariable Long idVehicle, @PathVariable Long idMechanic) {
        return vehiculeService.afecterMecanicien(idVehicle, idMechanic);
    }

    @GetMapping("/by/status")
    public List<Vehicule> getVehicleByStatus(@RequestParam Status status) {
        return vehiculeService.getVehiculeByStatus(status);
    }

    @GetMapping("/search")
    public List<Vehicule> recherche(@RequestParam String text) {
        return vehiculeService.rechercher(text);
    }
}