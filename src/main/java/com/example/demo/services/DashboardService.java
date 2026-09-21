package com.example.demo.services;

import com.example.demo.entities.Dashboard;
import com.example.demo.entities.Intervention;
import com.example.demo.enums.Status;
import com.example.demo.repositories.InterventionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final InterventionRepository interventionRepository;

    public DashboardService(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    public Dashboard getWorkshopStats() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(23, 59, 59);
        LocalDateTime now = LocalDateTime.now();

        long receivedToday = interventionRepository.countByDepositDateBetween(startOfToday, endOfToday);

        long inProgressDiagnostic = interventionRepository.countByStatus(Status.DIAGNOSTIC_IN_PROGRESS);

        long underRepair = interventionRepository.countByStatus(Status.UNDER_REPAIR);

        long completed = interventionRepository.countByStatus(Status.COMPLETED);

        List<Intervention> ongoingInterventions = interventionRepository.findByStatus(Status.UNDER_REPAIR);
        Map<Long, Long> workloadPerMechanic = ongoingInterventions.stream()
                .filter(i -> i.getMechanic() != null)
                .collect(Collectors.groupingBy(i -> i.getMechanic().getId(), Collectors.counting()));

        List<Intervention> delayedInterventions = interventionRepository.findDelayedInterventions(now, List.of(Status.RETURNED, Status.CANCELLED));

        return new Dashboard(receivedToday, inProgressDiagnostic, underRepair, completed, workloadPerMechanic, delayedInterventions);
    }
}
