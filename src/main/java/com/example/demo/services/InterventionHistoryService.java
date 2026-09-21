package com.example.demo.services;

import com.example.demo.entities.InterventionHistory;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.repositories.HistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class InterventionHistoryService implements IInterventionHistoryService {

    private final HistoryRepository interventionHistoryRepository;

    public InterventionHistoryService(HistoryRepository interventionHistoryRepository) {
        this.interventionHistoryRepository = interventionHistoryRepository;
    }

    @Override
    public List<InterventionHistory> getAllHistories() {
        return interventionHistoryRepository.findAll();
    }

    @Override
    public List<InterventionHistory> getHistoryByInterventionId(Long interventionId) {
        return interventionHistoryRepository.findByInterventionIdOrderByDateDesc(interventionId);
    }

    @Override
    public InterventionHistory createHistory(InterventionHistory interventionHistory) {
        if (interventionHistoryRepository.existsByInterventionId(interventionHistory.getIntervention().getId())) {
            throw new AllReadyExistException("History for this Intervention already exists");
        } else {
            if (interventionHistory.getDate() == null) {
                interventionHistory.setDate(LocalDateTime.now());
            }
            return interventionHistoryRepository.save(interventionHistory);
        }
    }

    @Override
    public List<InterventionHistory> getHistoriesByDate(LocalDateTime date) {
        LocalDate localDate = date.toLocalDate();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        return interventionHistoryRepository.findByDateBetween(startOfDay, endOfDay);
    }
}
