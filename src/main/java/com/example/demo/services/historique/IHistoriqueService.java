package com.example.demo.services.historique;

import com.example.demo.entities.HistoriqueIntervention;

import java.util.List;

public interface IHistoriqueService {
    List<HistoriqueIntervention> getAllHistoriques();
    List<HistoriqueIntervention> getHistoriqueByInterventionId(Long interventionId);
    HistoriqueIntervention createHistorique(HistoriqueIntervention historiqueIntervention);

}
