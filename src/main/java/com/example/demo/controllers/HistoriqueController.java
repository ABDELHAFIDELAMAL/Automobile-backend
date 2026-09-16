package com.example.demo.controllers;

import com.example.demo.entities.HistoriqueIntervention;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.historique.IHistoriqueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/historiques")
@CrossOrigin(origins = "*")
public class HistoriqueController {
    private final IHistoriqueService historiqueService;

    public HistoriqueController(IHistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllHistoriques() {
        List<HistoriqueIntervention> data = historiqueService.getAllHistoriques();
        ApiResponse response = new ApiResponse("Historiques fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/by/intervention/{interventionId}")
    public ResponseEntity<ApiResponse> getHistoriqueByInterventionId(@PathVariable Long interventionId) {
        List<HistoriqueIntervention> data = historiqueService.getHistoriqueByInterventionId(interventionId);
        ApiResponse response = new ApiResponse("Historiques for intervention fetched successfully", data, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createHistorique(@RequestBody HistoriqueIntervention historiqueIntervention) {
        try {
            HistoriqueIntervention data = historiqueService.createHistorique(historiqueIntervention);
            ApiResponse response = new ApiResponse("Historique created successfully", data, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            throw new AllReadyExistException(e.getMessage());
        }
    }



    @GetMapping(path = "/by/date")
    public ResponseEntity<ApiResponse> getHistoriquesByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateSimple) {

        LocalDateTime dateLocalDateTime = dateSimple.atStartOfDay();

        List<HistoriqueIntervention> data = historiqueService.getHistoriquesByDate(dateLocalDateTime);
        ApiResponse response = new ApiResponse("Success", data ,true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
