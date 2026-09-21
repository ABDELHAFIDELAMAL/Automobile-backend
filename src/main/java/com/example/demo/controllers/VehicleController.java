package com.example.demo.controllers;

import com.example.demo.entities.Vehicle;
import com.example.demo.enums.Status;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.IVehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    private final IVehicleService vehicleService;

    public VehicleController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllVehicles() {
        List<Vehicle> data = vehicleService.getAllVehicles();
        ApiResponse response = new ApiResponse("Vehicles fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle data = vehicleService.createVehicle(vehicle);
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
            vehicleService.deleteVehicle(id);
            ApiResponse response = new ApiResponse("Vehicle deleted successfully", null, true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        Vehicle data = vehicleService.updateVehicle(id, vehicle);
        ApiResponse response = new ApiResponse("Vehicle updated successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getVehicleById(@PathVariable Long id) {
        Vehicle data = vehicleService.getVehicleById(id);
        ApiResponse response = new ApiResponse("Vehicle details fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/by/matricule")
    public ResponseEntity<ApiResponse> getVehicleByLicensePlate(@RequestParam String matricule) {
        Vehicle data = vehicleService.getVehicleByMatricule(matricule);
        ApiResponse response = new ApiResponse("Vehicle fetched by matricule number", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/return/{id}")
    public ResponseEntity<ApiResponse> returnVehicle(@PathVariable Long id,
                                                     @RequestParam String username,
                                                     @RequestParam String userRole) {
        Vehicle data = vehicleService.returnVehicle(id, username, userRole);
        ApiResponse response = new ApiResponse("Vehicle returned successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/by/status")
    public ResponseEntity<ApiResponse> getVehicleByStatus(@RequestParam Status status) {
        List<Vehicle> data = vehicleService.getVehicleByStatus(status);
        ApiResponse response = new ApiResponse("Vehicles fetched by status", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(@RequestParam String text) {
        List<Vehicle> data = vehicleService.search(text);
        ApiResponse response = new ApiResponse("Search results fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
