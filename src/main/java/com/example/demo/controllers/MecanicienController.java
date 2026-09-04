package com.example.demo.controllers;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.mecanicien.IMecanicienService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mecaniciens")
@CrossOrigin("*")
public class MecanicienController {

    private final IMecanicienService mecanicienService;

    public MecanicienController(IMecanicienService mecanicienService) {
        this.mecanicienService = mecanicienService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllMechanicals() {
        try {
            List<Mecanicien> data = mecanicienService.getAllMecaniciens();
            return ResponseEntity.ok(new ApiResponse("Mechanics fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch mechanics", null, false));
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse> getMechanicalsDisponibles(@RequestParam boolean disponible) {
        try {
            List<Mecanicien> data = mecanicienService.getMecaniciensDisponibles(disponible);
            return ResponseEntity.ok(new ApiResponse("Available mechanics fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch available mechanics", null, false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMecanicienById(@PathVariable Long id) {
        try {
            Mecanicien data = mecanicienService.getMecanicienById(id);
            return ResponseEntity.ok(new ApiResponse("Mechanic fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Mechanic not found", null, false));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createMecanicien(@RequestBody Mecanicien mecanicien) {
        try {
            Mecanicien data = mecanicienService.createMecanicien(mecanicien);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Mechanic created successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to create mechanic", null, false));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMecanicien(@PathVariable Long id, @RequestBody Mecanicien mecanicien) {
        try {
            Mecanicien data = mecanicienService.updateMecanicien(id, mecanicien);
            return ResponseEntity.ok(new ApiResponse("Mechanic updated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to update mechanic", null, false));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMecanicien(@PathVariable Long id) {
        try {
            mecanicienService.deleteMecanicien(id);
            return ResponseEntity.ok(new ApiResponse("Mechanic deleted successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Failed to delete mechanic", null, false));
        }
    }

    @PatchMapping("/activer/{id}")
    public ResponseEntity<ApiResponse> activer(@PathVariable Long id) {
        try {
            Mecanicien data = mecanicienService.activer(id);
            return ResponseEntity.ok(new ApiResponse("Mechanic activated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to activate mechanic", null, false));
        }
    }

    @PatchMapping("/desactiver/{id}")
    public ResponseEntity<ApiResponse> desactiver(@PathVariable Long id) {
        try {
            Mecanicien data = mecanicienService.desactiver(id);
            return ResponseEntity.ok(new ApiResponse("Mechanic deactivated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to deactivate mechanic", null, false));
        }
    }

    @GetMapping("/{id}/interventions")
    public ResponseEntity<ApiResponse> getInterventions(@PathVariable Long id) {
        try {
            List<Intervention> data = mecanicienService.getInterventions(id);
            return ResponseEntity.ok(new ApiResponse("Interventions fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch interventions", null, false));
        }
    }

    @GetMapping("/charge")
    public ResponseEntity<ApiResponse> getCharge() {
        try {
            Map<Long, Integer> data = mecanicienService.getCharge();
            return ResponseEntity.ok(new ApiResponse("Workload charge fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch workload charge", null, false));
        }
    }
}
