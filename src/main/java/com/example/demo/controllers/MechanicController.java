package com.example.demo.controllers;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mechanic;
import com.example.demo.enums.Specialty;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.IMechanicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mechanics")
@CrossOrigin(origins = "*")
public class MechanicController {

    private final IMechanicService mechanicService;

    public MechanicController(IMechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMechanics() {
        List<Mechanic> data = mechanicService.getAllMechanics();
        ApiResponse response = new ApiResponse("Mechanics fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableMechanics(@RequestParam boolean available) {
        List<Mechanic> data = mechanicService.getAvailableMechanics(available);
        ApiResponse response = new ApiResponse("Available mechanics fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMechanicById(@PathVariable Long id) {
        Mechanic data = mechanicService.getMechanicById(id);
        ApiResponse response = new ApiResponse("Mechanic details fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createMechanic(@RequestBody Mechanic mechanic) {
        try {
            Mechanic data = mechanicService.createMechanic(mechanic);
            ApiResponse response = new ApiResponse("Mechanic created successfully", data, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            throw new AllReadyExistException(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateMechanic(@PathVariable Long id, @RequestBody Mechanic mechanic) {
        Mechanic data = mechanicService.updateMechanic(id, mechanic);
        ApiResponse response = new ApiResponse("Mechanic updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteMechanic(@PathVariable Long id) {
        mechanicService.deleteMechanic(id);
        ApiResponse response = new ApiResponse("Mechanic deleted successfully", null, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<ApiResponse> activate(@PathVariable Long id) {
        Mechanic data = mechanicService.activate(id);
        ApiResponse response = new ApiResponse("Mechanic activated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivate(@PathVariable Long id) {
        Mechanic data = mechanicService.deactivate(id);
        ApiResponse response = new ApiResponse("Mechanic deactivated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/interventions")
    public ResponseEntity<ApiResponse> getInterventions(@PathVariable Long id) {
        List<Intervention> data = mechanicService.getInterventions(id);
        ApiResponse response = new ApiResponse("Interventions for mechanic fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/workload")
    public ResponseEntity<ApiResponse> getWorkload() {
        Map<Long, Integer> data = mechanicService.getWorkload();
        ApiResponse response = new ApiResponse("Mechanics workload fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/specialty")
    public ResponseEntity<ApiResponse> getMechanicsBySpecialty(@RequestParam("specialty") String specialtyStr) {
        try {
            Specialty specialty = Specialty.valueOf(specialtyStr.toUpperCase());
            List<Mechanic> data = mechanicService.getMechanicsBySpecialty(specialty);
            ApiResponse response = new ApiResponse("Mechanics fetched successfully by specialty", data, true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ApiResponse errorResponse = new ApiResponse("Invalid specialty: " + specialtyStr, null, false);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
