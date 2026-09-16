package com.example.demo.services.historique;

import com.example.demo.entities.HistoriqueIntervention;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IHistoriqueService {
    List<HistoriqueIntervention> getAllHistoriques();
    List<HistoriqueIntervention> getHistoriqueByInterventionId(Long interventionId);
    HistoriqueIntervention createHistorique(HistoriqueIntervention historiqueIntervention);
    List<HistoriqueIntervention> getHistoriquesByDate(LocalDateTime date);

}
