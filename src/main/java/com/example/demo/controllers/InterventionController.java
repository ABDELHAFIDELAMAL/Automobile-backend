package com.example.demo.controllers;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.enums.Status;
import com.example.demo.enums.TypeIntervention;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.intervention.IInterventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/interventions")
@CrossOrigin(origins = "*")
public class InterventionController {
    private final IInterventionService interventionService;

    public InterventionController(IInterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllInterventions() {
        List<Intervention> data = interventionService.getAllInterventions();
        ApiResponse response = new ApiResponse("Interventions fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> getInterventionById(@PathVariable Long id) {
        Intervention data = interventionService.getInterventionById(id);
        ApiResponse response = new ApiResponse("Interventions fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createIntervention(@RequestBody Intervention intervention) {
        try {
            Intervention data = interventionService.createIntervention(intervention);
            ApiResponse response = new ApiResponse("Intervention created successfully", data, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            throw new AllReadyExistException(e.getMessage());
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updateIntervention(@PathVariable Long id, @RequestBody Intervention intervention) {
        try {
            Intervention data = interventionService.updateIntervention(id, intervention);
            ApiResponse response = new ApiResponse("Intervention updated successfully", data, true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AllReadyExistException e) {
            throw new AllReadyExistException(e.getMessage());
        }
    }

    @PatchMapping(path = "assign/{id}")
    public ResponseEntity<ApiResponse> assignMecanicien(@PathVariable Long id, @RequestBody Mecanicien mecanicien) {
        Intervention data = interventionService.assignMecanicien(id, mecanicien);
        ApiResponse response = new ApiResponse("Mechanic assigned successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/setcout/{id}")
    public ResponseEntity<ApiResponse> setCoutEstime(@PathVariable Long id, @RequestParam Double cout) {
        Intervention data = interventionService.setCoutEstime(id, cout);
        ApiResponse response = new ApiResponse("Estimated cost updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/ajouter/diagnostic/{id}")
    public ResponseEntity<ApiResponse> addDiagnostic(@PathVariable Long id, @RequestParam String diagnostic) {
        Intervention data = interventionService.addDiagnostic(id, diagnostic);
        ApiResponse response = new ApiResponse("Diagnostic added successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path = "/change/status/{id}")
    public ResponseEntity<ApiResponse> changerStatus(@PathVariable Long id, @RequestBody Status statusIntervention) {
        Intervention data = interventionService.changerStatus(id, statusIntervention);
        ApiResponse response = new ApiResponse("Status updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path = "/terminer/{id}")
    public ResponseEntity<ApiResponse> terminer(@PathVariable Long id) {
        Intervention data = interventionService.terminer(id);
        ApiResponse response = new ApiResponse("Intervention marked as finished", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path = "/restituer/{id}")
    public ResponseEntity<ApiResponse> restituer(@PathVariable Long id) {
        Intervention data = interventionService.restituer(id);
        ApiResponse response = new ApiResponse("Vehicle returned successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/mecanicien/{id}")
    public ResponseEntity<ApiResponse> getInterventionByMecanicien(@PathVariable Long id) {
        List<Intervention> data = interventionService.getInterventionByMecanicien(id);
        ApiResponse response = new ApiResponse("Interventions fetched for mechanic", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/vehicule/{id}")
    public ResponseEntity<ApiResponse> getInterventionByVehicule(@PathVariable Long id) {
        List<Intervention> data = interventionService.getInterventionByVehicule(id);
        ApiResponse response = new ApiResponse("Interventions fetched for vehicle", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/en/retard")
    public ResponseEntity<ApiResponse> getEnRetard() {
        List<Intervention> data = interventionService.getEnRetard();
        ApiResponse response = new ApiResponse("Delayed interventions fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/calculer/cout/total/{id}")
    public ResponseEntity<ApiResponse> calculerCoutTotal(@PathVariable Long id) {
        Double data = interventionService.calculerCoutTotal(id);
        ApiResponse response = new ApiResponse("Total cost calculated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/type/{id}")
    public ResponseEntity<ApiResponse> getInterventionsByType(@RequestBody TypeIntervention type) {
        List<Intervention> data = interventionService.getInterventionsByType(type);
        ApiResponse response = new ApiResponse("Interventions fetched by type", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
