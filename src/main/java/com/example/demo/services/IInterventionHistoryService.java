package com.example.demo.services;


import com.example.demo.entities.InterventionHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface IInterventionHistoryService {
    List<InterventionHistory> getAllHistories();
    List<InterventionHistory> getHistoryByInterventionId(Long interventionId);
    InterventionHistory createHistory(InterventionHistory interventionHistory);
    List<InterventionHistory> getHistoriesByDate(LocalDateTime date);
}
