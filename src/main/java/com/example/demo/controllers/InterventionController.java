package com.example.demo.controllers;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.enums.Status;
import com.example.demo.enums.TypeIntervention;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.intervention.IInterventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/interventions")
@CrossOrigin(origins = "http://localhost:4200")
public class InterventionController {
    private final IInterventionService interventionService;

    public InterventionController(IInterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse> getAllInterventions() {
        try {
            List<Intervention> data = interventionService.getAllInterventions();
            return ResponseEntity.ok(new ApiResponse("Interventions fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch interventions", null, false));
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createIntervention(@RequestBody Intervention intervention) {
        try {
            Intervention data = interventionService.createIntervention(intervention);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Intervention created successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to create intervention", null, false));
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updateIntervention(@PathVariable Long id, @RequestBody Intervention intervention) {
        try {
            Intervention data = interventionService.updateIntervention(id, intervention);
            return ResponseEntity.ok(new ApiResponse("Intervention updated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to update intervention", null, false));
        }
    }

    @PatchMapping(path = "assign/{id}")
    public ResponseEntity<ApiResponse> assignMecanicien(@PathVariable Long id, @RequestBody Mecanicien mecanicien) {
        try {
            Intervention data = interventionService.assignMecanicien(id, mecanicien);
            return ResponseEntity.ok(new ApiResponse("Mechanic assigned successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to assign mechanic", null, false));
        }
    }

    @PostMapping(path = "/setcout/{id}")
    public ResponseEntity<ApiResponse> setCoutEstime(@PathVariable Long id, @RequestParam Double cout) {
        try {
            Intervention data = interventionService.setCoutEstime(id, cout);
            return ResponseEntity.ok(new ApiResponse("Estimated cost set successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to set estimated cost", null, false));
        }
    }

    @PostMapping(path = "/ajouter/diagnostic/{id}")
    public ResponseEntity<ApiResponse> addDiagnostic(@PathVariable Long id, @RequestParam String diagnostic) {
        try {
            Intervention data = interventionService.addDiagnostic(id, diagnostic);
            return ResponseEntity.ok(new ApiResponse("Diagnostic added successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to add diagnostic", null, false));
        }
    }

    @PatchMapping(path = "/change/status/{id}")
    public ResponseEntity<ApiResponse> changerStatus(@PathVariable Long id, @RequestBody Status statusIntervention) {
        try {
            Intervention data = interventionService.changerStatus(id, statusIntervention);
            return ResponseEntity.ok(new ApiResponse("Status updated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to update status", null, false));
        }
    }

    @PatchMapping(path = "/terminer/{id}")
    public ResponseEntity<ApiResponse> terminer(@PathVariable Long id) {
        try {
            Intervention data = interventionService.terminer(id);
            return ResponseEntity.ok(new ApiResponse("Intervention finished successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to finish intervention", null, false));
        }
    }

    @PatchMapping(path = "/restituer/{id}")
    public ResponseEntity<ApiResponse> restituer(@PathVariable Long id) {
        try {
            Intervention data = interventionService.restituer(id);
            return ResponseEntity.ok(new ApiResponse("Vehicle released successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to release vehicle", null, false));
        }
    }

    @GetMapping(path = "/by/mecanicien/{id}")
    public ResponseEntity<ApiResponse> getInterventionByMecanicien(@PathVariable Long id) {
        try {
            List<Intervention> data = interventionService.getInterventionByMecanicien(id);
            return ResponseEntity.ok(new ApiResponse("Interventions fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch interventions for mechanic", null, false));
        }
    }

    @GetMapping(path = "/by/vehicule/{id}")
    public ResponseEntity<ApiResponse> getInterventionByVehicule(@PathVariable Long id) {
        try {
            List<Intervention> data = interventionService.getInterventionByVehicule(id);
            return ResponseEntity.ok(new ApiResponse("Interventions fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch interventions for vehicle", null, false));
        }
    }

    @GetMapping(path = "/en/retard")
    public ResponseEntity<ApiResponse> getEnRetard() {
        try {
            List<Intervention> data = interventionService.getEnRetard();
            return ResponseEntity.ok(new ApiResponse("Delayed interventions fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch delayed interventions", null, false));
        }
    }

    @GetMapping(path = "/calculer/cout/total/{id}")
    public ResponseEntity<ApiResponse> calculerCoutTotal(@PathVariable Long id) {
        try {
            Double data = interventionService.calculerCoutTotal(id);
            return ResponseEntity.ok(new ApiResponse("Total cost calculated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to calculate total cost", null, false));
        }
    }

    @GetMapping(path = "/by/type/{id}")
    public ResponseEntity<ApiResponse> getInterventionsByType(@RequestBody TypeIntervention type) {
        try {
            List<Intervention> data = interventionService.getInterventionsByType(type);
            return ResponseEntity.ok(new ApiResponse("Interventions fetched successfully by type", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch interventions by type", null, false));
        }
    }
}
