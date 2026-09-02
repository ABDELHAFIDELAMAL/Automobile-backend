package com.example.demo.controllers;

import com.example.demo.enums.Status;
import com.example.demo.entities.Vehicule;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.vehicule.IVehiculeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllVehicles() {
        try {
            List<Vehicule> data = vehiculeService.getAllVehicules();
            return ResponseEntity.ok(new ApiResponse("Vehicles fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch vehicles", null, false));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createVehicule(@RequestBody Vehicule vehicule) {
        try {
            Vehicule data = vehiculeService.createVehicule(vehicule);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Vehicle created successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to create vehicle", null, false));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable Long id) {
        try {
            vehiculeService.deleteVehicule(id);
            return ResponseEntity.ok(new ApiResponse("Vehicle deleted successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Failed to delete vehicle", null, false));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateVehicule(@PathVariable Long id, @RequestBody Vehicule vehicule) {
        try {
            Vehicule data = vehiculeService.updateVehicule(id, vehicule);
            return ResponseEntity.ok(new ApiResponse("Vehicle updated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to update vehicle", null, false));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getVehicleById(@PathVariable Long id) {
        try {
            Vehicule data = vehiculeService.getVehiculeById(id);
            return ResponseEntity.ok(new ApiResponse("Vehicle fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Vehicle not found", null, false));
        }
    }

    @GetMapping("/by/matricule")
    public ResponseEntity<ApiResponse> getVehiculeByMatricule(@RequestParam String matricule) {
        try {
            Vehicule data = vehiculeService.getVehiculeByMatricule(matricule);
            return ResponseEntity.ok(new ApiResponse("Vehicle fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Vehicle not found with this matricule", null, false));
        }
    }

    @PatchMapping("/restituer/{id}")
    public ResponseEntity<ApiResponse> restituerVehicule(@PathVariable Long id,
                                                         @RequestParam String username, @RequestParam String userRole) {
        try {
            Vehicule data = vehiculeService.restituerVehicule(id, username, userRole);
            return ResponseEntity.ok(new ApiResponse("Vehicle returned successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to return vehicle", null, false));
        }
    }

    @PatchMapping("/{idVehicle}/affecter/mecanicien/{idMechanic}")
    public ResponseEntity<ApiResponse> affecterMecanicien(@PathVariable Long idVehicle, @PathVariable Long idMechanic) {
        try {
            Vehicule data = vehiculeService.afecterMecanicien(idVehicle, idMechanic);
            return ResponseEntity.ok(new ApiResponse("Mechanic assigned successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to assign mechanic", null, false));
        }
    }

    @GetMapping("/by/status")
    public ResponseEntity<ApiResponse> getVehicleByStatus(@RequestParam Status status) {
        try {
            List<Vehicule> data = vehiculeService.getVehiculeByStatus(status);
            return ResponseEntity.ok(new ApiResponse("Vehicles fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch vehicles by status", null, false));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> recherche(@RequestParam String text) {
        try {
            List<Vehicule> data = vehiculeService.rechercher(text);
            return ResponseEntity.ok(new ApiResponse("Search completed successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Search failed", null, false));
        }
    }
}
