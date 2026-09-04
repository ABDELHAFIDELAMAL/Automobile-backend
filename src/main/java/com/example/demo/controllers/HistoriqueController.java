package com.example.demo.controllers;

import com.example.demo.entities.HistoriqueIntervention;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.historique.IHistoriqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/historiques")
@CrossOrigin("*")
public class HistoriqueController {
    private final IHistoriqueService historiqueService;

    public HistoriqueController(IHistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse> getAllHistoriques() {
        try {
            List<HistoriqueIntervention> data = historiqueService.getAllHistoriques();
            return ResponseEntity.ok(new ApiResponse("Historics fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch historics", null, false));
        }
    }

    @GetMapping(path = "/by/intervention/{interventionId}")
    public ResponseEntity<ApiResponse> getHistoriqueByInterventionId(@PathVariable Long interventionId) {
        try {
            List<HistoriqueIntervention> data = historiqueService.getHistoriqueByInterventionId(interventionId);
            return ResponseEntity.ok(new ApiResponse("Historics fetched successfully for this intervention", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch historics for this intervention", null, false));
        }
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse> createHistorique(@RequestBody HistoriqueIntervention historiqueIntervention) {
        try {
            HistoriqueIntervention data = historiqueService.createHistorique(historiqueIntervention);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Historic created successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to create historic", null, false));
        }
    }
}
