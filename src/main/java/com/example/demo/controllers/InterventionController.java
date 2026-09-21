package com.example.demo.controllers;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mechanic;
import com.example.demo.enums.Priority;
import com.example.demo.enums.Status;
import com.example.demo.enums.InterventionType;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.IInterventionService;
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
        ApiResponse response = new ApiResponse("Intervention fetched successfully", data, true);
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

    @PatchMapping(path = "/assign/{id}")
    public ResponseEntity<ApiResponse> assignMechanic(@PathVariable Long id, @RequestBody Mechanic mechanic) {
        Intervention data = interventionService.assignMechanic(id, mechanic);
        ApiResponse response = new ApiResponse("Mechanic assigned successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/set-cost/{id}")
    public ResponseEntity<ApiResponse> setEstimatedCost(@PathVariable Long id, @RequestParam("estimatedCost") Double cost) {
        Intervention data = interventionService.setEstimatedCost(id, cost);
        ApiResponse response = new ApiResponse("Estimated cost updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-diagnostic/{id}")
    public ResponseEntity<ApiResponse> addDiagnostic(@PathVariable Long id, @RequestParam String diagnostic) {
        Intervention data = interventionService.addDiagnostic(id, diagnostic);
        ApiResponse response = new ApiResponse("Diagnostic added successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path = "/change-status/{id}")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable Long id, @RequestBody Status statusIntervention, @RequestParam String author) {
        Intervention data = interventionService.changeStatus(id, statusIntervention, author);
        ApiResponse response = new ApiResponse("Status updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path = "/complete/{id}")
    public ResponseEntity<ApiResponse> complete(@PathVariable Long id) {
        Intervention data = interventionService.complete(id);
        ApiResponse response = new ApiResponse("Intervention marked as completed", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(path = "/return/{id}")
    public ResponseEntity<ApiResponse> returnIntervention(@PathVariable Long id) {
        Intervention data = interventionService.returnIntervention(id);
        ApiResponse response = new ApiResponse("Vehicle returned successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/mechanic/{id}")
    public ResponseEntity<ApiResponse> getInterventionsByMechanic(@PathVariable Long id) {
        List<Intervention> data = interventionService.getInterventionsByMechanic(id);
        ApiResponse response = new ApiResponse("Interventions fetched for mechanic", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/vehicle/{id}")
    public ResponseEntity<ApiResponse> getInterventionsByVehicle(@PathVariable Long id) {
        List<Intervention> data = interventionService.getInterventionsByVehicle(id);
        ApiResponse response = new ApiResponse("Interventions fetched for vehicle", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/delayed")
    public ResponseEntity<ApiResponse> getDelayedInterventions() {
        List<Intervention> data = interventionService.getDelayedInterventions();
        ApiResponse response = new ApiResponse("Delayed interventions fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/calculate-total-cost")
    public ResponseEntity<ApiResponse> calculateTotalCost() {
        Double data = interventionService.calculateTotalCost();
        ApiResponse response = new ApiResponse("Total cost calculated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/type")
    public ResponseEntity<ApiResponse> getInterventionsByType(@RequestParam("type") String typeStr) {
        InterventionType type = InterventionType.valueOf(typeStr.toUpperCase());
        List<Intervention> data = interventionService.getInterventionsByType(type);
        ApiResponse response = new ApiResponse("Interventions fetched by type", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/priority")
    public ResponseEntity<ApiResponse> getInterventionsByPriority(@RequestParam("priority") String priorityStr) {
        Priority priority = Priority.valueOf(priorityStr.toUpperCase());
        List<Intervention> data = interventionService.getInterventionsByPriority(priority);
        ApiResponse response = new ApiResponse("Interventions fetched by priority successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
