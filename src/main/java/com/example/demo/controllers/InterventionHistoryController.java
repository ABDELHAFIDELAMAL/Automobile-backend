package com.example.demo.controllers;

import com.example.demo.entities.InterventionHistory;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.IInterventionHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/histories")
@CrossOrigin(origins = "*")
public class InterventionHistoryController {

    private final IInterventionHistoryService interventionHistoryService;

    public InterventionHistoryController(IInterventionHistoryService interventionHistoryService) {
        this.interventionHistoryService = interventionHistoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllHistories() {
        List<InterventionHistory> data = interventionHistoryService.getAllHistories();
        ApiResponse response = new ApiResponse("Histories fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/intervention/{interventionId}")
    public ResponseEntity<ApiResponse> getHistoryByInterventionId(@PathVariable Long interventionId) {
        List<InterventionHistory> data = interventionHistoryService.getHistoryByInterventionId(interventionId);
        ApiResponse response = new ApiResponse("Histories for intervention fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createHistory(@RequestBody InterventionHistory interventionHistory) {
        try {
            InterventionHistory data = interventionHistoryService.createHistory(interventionHistory);
            ApiResponse response = new ApiResponse("History created successfully", data, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            throw new AllReadyExistException(e.getMessage());
        }
    }

    @GetMapping(path = "/by/date")
    public ResponseEntity<ApiResponse> getHistoriesByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateSimple) {

        LocalDateTime dateLocalDateTime = dateSimple.atStartOfDay();
        List<InterventionHistory> data = interventionHistoryService.getHistoriesByDate(dateLocalDateTime);
        ApiResponse response = new ApiResponse("Histories fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
