package com.example.demo.controllers;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.mecanicien.IMecanicienService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mecaniciens")
@CrossOrigin(origins = "*")
public class MecanicienController {

    private final IMecanicienService mecanicienService;

    public MecanicienController(IMecanicienService mecanicienService) {
        this.mecanicienService = mecanicienService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMechanicals() {
        List<Mecanicien> data = mecanicienService.getAllMecaniciens();
        ApiResponse response = new ApiResponse("Mechanics fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse> getMechanicalsDisponibles(@RequestParam boolean disponible) {
        List<Mecanicien> data = mecanicienService.getMecaniciensDisponibles(disponible);
        ApiResponse response = new ApiResponse("Available mechanics fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMecanicienById(@PathVariable Long id) {
        Mecanicien data = mecanicienService.getMecanicienById(id);
        ApiResponse response = new ApiResponse("Mechanic details fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createMecanicien(@RequestBody Mecanicien mecanicien) {
        try {
            Mecanicien data = mecanicienService.createMecanicien(mecanicien);
            ApiResponse response = new ApiResponse("Mechanic created successfully", data, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            throw new AllReadyExistException(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMecanicien(@PathVariable Long id, @RequestBody Mecanicien mecanicien) {
        Mecanicien data = mecanicienService.updateMecanicien(id, mecanicien);
        ApiResponse response = new ApiResponse("Mechanic updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMecanicien(@PathVariable Long id) {
        mecanicienService.deleteMecanicien(id);
        ApiResponse response = new ApiResponse("Mechanic deleted successfully", null, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/activer/{id}")
    public ResponseEntity<ApiResponse> activer(@PathVariable Long id) {
        Mecanicien data = mecanicienService.activer(id);
        ApiResponse response = new ApiResponse("Mechanic activated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/desactiver/{id}")
    public ResponseEntity<ApiResponse> desactiver(@PathVariable Long id) {
        Mecanicien data = mecanicienService.desactiver(id);
        ApiResponse response = new ApiResponse("Mechanic deactivated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/interventions")
    public ResponseEntity<ApiResponse> getInterventions(@PathVariable Long id) {
        List<Intervention> data = mecanicienService.getInterventions(id);
        ApiResponse response = new ApiResponse("Interventions for mechanic fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/charge")
    public ResponseEntity<ApiResponse> getCharge() {
        Map<Long, Integer> data = mecanicienService.getCharge();
        ApiResponse response = new ApiResponse("Mechanics load workload fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
