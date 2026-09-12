package com.example.demo.services.dashboard;


import com.example.demo.dto.DashboardDto;
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

    public DashboardDto getAtelierStats() {
        LocalDateTime debutAujourdhui = LocalDate.now().atStartOfDay();
        LocalDateTime finAujourdhui = LocalDate.now().atTime(23, 59, 59);
        LocalDateTime maintenant = LocalDateTime.now();

        long recuesAujourdhui = interventionRepository.countByDateDepotBetween(debutAujourdhui, finAujourdhui);

        long enDiagnostic = interventionRepository.countByStatus(Status.DIAGNOSTIC_EN_COURS);

        long enReparation = interventionRepository.countByStatus(Status.EN_REPARATION);

        long terminees = interventionRepository.countByStatus(Status.TERMINEE);

        List<Intervention> enCours = interventionRepository.findByStatus(Status.EN_REPARATION);
        Map<Long, Long> chargeParMecanicien = enCours.stream()
                .filter(i -> i.getMecanicien() != null)
                .collect(Collectors.groupingBy(i -> i.getMecanicien().getId(), Collectors.counting()));

        List<Intervention> retards = interventionRepository.findRetards(maintenant, List.of(Status.RESTITUEE, Status.ANNULEE));

        return new DashboardDto(recuesAujourdhui, enDiagnostic, enReparation, terminees, chargeParMecanicien, retards);
    }
}

