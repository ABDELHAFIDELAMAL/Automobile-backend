package com.example.demo.controllers;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.services.mecanicien.IMecanicienService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mecaniciens")
@CrossOrigin(origins = "http://localhost:4200")
public class MecanicienController {

    private final IMecanicienService mecanicienService;

    public MecanicienController(IMecanicienService mecanicienService) {
        this.mecanicienService = mecanicienService;
    }

    @GetMapping
    public List<Mecanicien> getAllMechanicals() {
        return mecanicienService.getAllMecaniciens();
    }

    @GetMapping("/disponibles")
    public List<Mecanicien> getMechanicalsDisponibles(@RequestParam boolean disponible) {
        return mecanicienService.getMecaniciensDisponibles(disponible);
    }

    @GetMapping("/{id}")
    public Mecanicien getMecanicienById(
            @PathVariable Long id) {
        return mecanicienService.getMecanicienById(id);
    }

    @PostMapping("/create")
    public Mecanicien createMecanicien(@RequestBody Mecanicien mecanicien) {
        return mecanicienService.createMecanicien(mecanicien);
    }

    @PutMapping("/update/{id}")
    public Mecanicien updateMecanicien(@PathVariable Long id, @RequestBody Mecanicien mecanicien) {
        return mecanicienService.updateMecanicien(id, mecanicien);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMecanicien(@PathVariable Long id) {
        mecanicienService.deleteMecanicien(id);
    }

    @PatchMapping("/activer/{id}")
    public Mecanicien activer(@PathVariable Long id) {
        return mecanicienService.activer(id);
    }

    @PatchMapping("/desactiver/{id}")
    public Mecanicien desactiver(@PathVariable Long id) {
        return mecanicienService.desactiver(id);
    }

    @GetMapping("/{id}/interventions")
    public List<Intervention> getInterventions(@PathVariable Long id) {
        return mecanicienService.getInterventions(id);
    }

    @GetMapping("/charge")
    public Map<Long, Integer> getCharge() {
        return mecanicienService.getCharge();
    }
}