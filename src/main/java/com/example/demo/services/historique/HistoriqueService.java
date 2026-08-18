package com.example.demo.services.historique;

import com.example.demo.entities.HistoriqueIntervention;
import com.example.demo.repositories.HistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoriqueService implements IHistoriqueService {

    private final HistoriqueRepository historiqueRepository;

    public HistoriqueService(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    @Override
    public List<HistoriqueIntervention> getAllHistoriques() {
        return historiqueRepository.findAll();
    }

    @Override
    public List<HistoriqueIntervention> getHistoriqueByInterventionId(Long interventionId) {
        return historiqueRepository.findByInterventionIdOrderByDateDesc(interventionId);
    }

    @Override
    public HistoriqueIntervention createHistorique(HistoriqueIntervention historiqueIntervention) {
        if (historiqueIntervention.getDate() == null) {
            historiqueIntervention.setDate(LocalDateTime.now());
        }
        return historiqueRepository.save(historiqueIntervention);
    }
}
