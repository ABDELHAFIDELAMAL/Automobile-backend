package com.example.demo.controllers;

import com.example.demo.entities.HistoriqueIntervention;
import com.example.demo.services.historique.HistoriqueService;
import com.example.demo.services.historique.IHistoriqueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/historiques")
@CrossOrigin(origins = "http://localhost:4200")
public class HistoriqueController  {
    private final IHistoriqueService historiqueService;

    public HistoriqueController(IHistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @GetMapping
    public List<HistoriqueIntervention> getAllHistoriques() {
        return historiqueService.getAllHistoriques();
    }

    @GetMapping(path = "/by/intervention/{interventionId}")
    public List<HistoriqueIntervention> getHistoriqueByInterventionId(@PathVariable Long interventionId) {
        return historiqueService.getHistoriqueByInterventionId(interventionId);
    }

    @PostMapping(path = "/create")
    public HistoriqueIntervention createHistorique(@RequestBody HistoriqueIntervention historiqueIntervention) {
        return historiqueService.createHistorique(historiqueIntervention);
    }
}
