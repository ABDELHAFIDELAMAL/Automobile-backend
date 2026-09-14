package com.example.demo.controllers;

import com.example.demo.entities.Vehicule;
import com.example.demo.enums.Status;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.vehicule.IVehiculeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicules")
@CrossOrigin(origins = "*")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    public VehiculeController(IVehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllVehicles() {
        List<Vehicule> data = vehiculeService.getAllVehicules();
        ApiResponse response = new ApiResponse("Vehicles fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createVehicule(@RequestBody Vehicule vehicule) {
        try {
            Vehicule data = vehiculeService.createVehicule(vehicule);
            ApiResponse response = new ApiResponse("Vehicle created successfully", data, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            ApiResponse response = new ApiResponse(e.getMessage(), null, false);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable Long id) {
        try {
            vehiculeService.deleteVehicule(id);
            ApiResponse response = new ApiResponse("Vehicle deleted successfully", null, true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateVehicule(@PathVariable Long id, @RequestBody Vehicule vehicule) {
        Vehicule data = vehiculeService.updateVehicule(id, vehicule);
        ApiResponse response = new ApiResponse("Vehicle updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getVehicleById(@PathVariable Long id) {
        Vehicule data = vehiculeService.getVehiculeById(id);
        ApiResponse response = new ApiResponse("Vehicle details fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/by/matricule")
    public ResponseEntity<ApiResponse> getVehiculeByMatricule(@RequestParam String matricule) {
        Vehicule data = vehiculeService.getVehiculeByMatricule(matricule);
        ApiResponse response = new ApiResponse("Vehicle fetched by registration number", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/restituer/{id}")
    public ResponseEntity<ApiResponse> restituerVehicule(@PathVariable Long id,
                                                         @RequestParam String username, @RequestParam String userRole) {
        Vehicule data = vehiculeService.restituerVehicule(id, username, userRole);
        ApiResponse response = new ApiResponse("Vehicle returned successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{idVehicle}/affecter/mecanicien/{idMechanic}")
    public ResponseEntity<ApiResponse> affecterMecanicien(@PathVariable Long idVehicle, @PathVariable Long idMechanic) {
        Vehicule data = vehiculeService.afecterMecanicien(idVehicle, idMechanic);
        ApiResponse response = new ApiResponse("Mechanic assigned to vehicle successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/by/status")
    public ResponseEntity<ApiResponse> getVehicleByStatus(@RequestParam Status status) {
        List<Vehicule> data = vehiculeService.getVehiculeByStatus(status);
        ApiResponse response = new ApiResponse("Vehicles fetched by status", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/recherche")
    public ResponseEntity<ApiResponse> recherche(@RequestParam String text) {
        List<Vehicule> data = vehiculeService.rechercher(text);
        ApiResponse response = new ApiResponse("Search results fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
