package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.enums.Priority;
import com.example.demo.enums.Status;
import com.example.demo.enums.InterventionType;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.repositories.HistoryRepository;
import com.example.demo.repositories.InterventionRepository;
import com.example.demo.repositories.MechanicRepository;
import com.example.demo.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.enums.Status.*;

@Service
@Transactional
@Slf4j
public class InterventionService implements IInterventionService {

    private final InterventionRepository interventionRepository;
    private final MechanicRepository mechanicRepository;
    private final HistoryRepository historyRepository;
    private final VehicleRepository vehicleRepository;

    public InterventionService(
            InterventionRepository interventionRepository,
            MechanicRepository mechanicRepository,
            HistoryRepository historyRepository,
            VehicleRepository vehicleRepository) {

        this.interventionRepository = interventionRepository;
        this.mechanicRepository = mechanicRepository;
        this.historyRepository = historyRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    public Intervention getInterventionById(Long id) {
        return interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found id " + id));
    }

    @Override
    public Intervention createIntervention(Intervention intervention) {

        boolean exist = interventionRepository.existsByVehicleIdAndTypeAndDescription(
                intervention.getVehicle().getId(),
                intervention.getType(),
                intervention.getDescription()
        );

        if (exist) {
            throw new AllReadyExistException("Intervention already exists with id " + intervention.getId());
        } else {
            log.warn("Set vehicle and mechanic into intervention !");
            return interventionRepository.save(intervention);
        }
    }

    @Override
    public Intervention updateIntervention(Long id, Intervention intervention) {

        Intervention interv = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        interv.setDiagnostic(intervention.getDiagnostic());
        interv.setType(intervention.getType());
        interv.setDescription(intervention.getDescription());
        interv.setStatus(intervention.getStatus());
        interv.setClosureDate(intervention.getClosureDate());
        interv.setMechanic(intervention.getMechanic());
        interv.setPriority(intervention.getPriority());
        interv.setEstimatedReturnDate(intervention.getEstimatedReturnDate());
        interv.setDepositDate(intervention.getDepositDate());
        interv.setEstimatedCost(intervention.getEstimatedCost());

        return interventionRepository.save(interv);
    }

    @Override
    public Intervention assignMechanic(Long id, Mechanic mechanic) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        Mechanic mechanic1 = mechanicRepository.findById(mechanic.getId())
                .orElseThrow(() ->
                        new RuntimeException("Mechanic not found with id : " + mechanic.getId()));

        if (!mechanic1.isAvailable()) {
            throw new IllegalStateException("This mechanic is currently not available.");
        }

        intervention.setMechanic(mechanic1);

        if (intervention.getStatus() == QUOTATION_TO_VALIDATE) {
            intervention.setStatus(UNDER_REPAIR);
        }

        mechanic1.setAvailable(false);
        mechanicRepository.save(mechanic1);

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention setEstimatedCost(Long id, Double cost) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (cost == null || cost <= 0) {
            throw new IllegalArgumentException("Estimated cost must be greater than 0");
        }

        intervention.setEstimatedCost(cost);

        if (intervention.getStatus() == DIAGNOSTIC_IN_PROGRESS) {
            intervention.setStatus(QUOTATION_TO_VALIDATE);
        }

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention addDiagnostic(Long id, String diagnostic) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (intervention.getStatus() != RECEIVED) {
            throw new IllegalStateException("Cannot add diagnostic");
        }

        Status previousStatus = intervention.getStatus();
        Status newStatus = DIAGNOSTIC_IN_PROGRESS;

        intervention.setDiagnostic(diagnostic);
        intervention.setStatus(newStatus);

        Intervention saved = interventionRepository.save(intervention);

        InterventionHistory history = new InterventionHistory();
        history.setIntervention(saved);
        history.setOldStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setComment("Technical diagnostic added by mechanic.");
        history.setAuthor(
                saved.getMechanic() != null
                        ? saved.getMechanic().getName()
                        : "SYSTEM");
        history.setDate(LocalDateTime.now());

        historyRepository.save(history);

        return saved;
    }

    @Override
    public Intervention changeStatus(Long id, Status newStatus, String author) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        Status currentStatus = intervention.getStatus();

        boolean valid = switch (currentStatus) {
            case RECEIVED -> newStatus == Status.DIAGNOSTIC_IN_PROGRESS;
            case DIAGNOSTIC_IN_PROGRESS -> newStatus == Status.QUOTATION_TO_VALIDATE;
            case QUOTATION_TO_VALIDATE -> newStatus == Status.UNDER_REPAIR;
            case UNDER_REPAIR -> newStatus == Status.COMPLETED;
            case COMPLETED -> newStatus == Status.RETURNED;
            case RETURNED -> false;
            default -> false;
        };

        if ("COMPLETED".equals(intervention.getStatus())) {
            mechanicRepository.findById(intervention.getMechanic().getId())
                    .ifPresent(mechanic -> {
                        mechanic.setAvailable(true);
                        mechanicRepository.save(mechanic);
                    });
        }

        if (!valid) {
            throw new IllegalStateException("Transition from " + currentStatus + " to " + newStatus + " forbidden.");
        }

        InterventionHistory history = new InterventionHistory();
        history.setIntervention(intervention);
        history.setOldStatus(currentStatus);
        history.setNewStatus(newStatus);
        history.setDate(LocalDateTime.now());
        history.setAuthor(author);
        historyRepository.save(history);

        intervention.setStatus(newStatus);

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention complete(Long id) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (intervention.getStatus() != UNDER_REPAIR) {
            throw new IllegalStateException("Intervention must be IN_REPAIR");
        }

        Status previousStatus = intervention.getStatus();

        intervention.setStatus(Status.COMPLETED);
        intervention.setClosureDate(LocalDate.now().atStartOfDay());

        interventionRepository.save(intervention);

        InterventionHistory history = new InterventionHistory();
        history.setIntervention(intervention);
        history.setOldStatus(previousStatus);
        history.setNewStatus(Status.COMPLETED);
        history.setComment("Intervention completed");
        history.setAuthor(
                intervention.getMechanic() != null
                        ? intervention.getMechanic().getName()
                        : "SYSTEM");
        history.setDate(LocalDateTime.now());

        historyRepository.save(history);

        return intervention;
    }

    @Override
    public Intervention returnIntervention(Long id) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found with id : " + id));

        if (intervention.getStatus() != Status.COMPLETED) {
            throw new IllegalStateException("The intervention must be COMPLETED before return");
        }

        Status oldStatus = intervention.getStatus();

        intervention.setStatus(Status.RETURNED);
        intervention.setClosureDate(LocalDateTime.now());

        if (intervention.getMechanic() != null) {
            Mechanic mechanic = intervention.getMechanic();
            mechanic.setAvailable(true);
            mechanicRepository.save(mechanic);
        }

        Intervention savedIntervention = interventionRepository.save(intervention);

        InterventionHistory history = new InterventionHistory();
        history.setIntervention(savedIntervention);
        history.setOldStatus(oldStatus);
        history.setNewStatus(Status.RETURNED);
        history.setComment("Vehicle returned to the client and mechanic released.");
        history.setAuthor(savedIntervention.getMechanic() != null ? savedIntervention.getMechanic().getName() : "SYSTEM");
        history.setDate(LocalDateTime.now());

        historyRepository.save(history);

        return savedIntervention;
    }


    @Override
    public Intervention returnVehicle(Long id) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Intervention not found with id : " + id));

        if (intervention.getStatus() != Status.COMPLETED) {
            throw new IllegalStateException("Intervention must be COMPLETED before return");
        }

        Status previousStatus = intervention.getStatus();

        intervention.setStatus(Status.RETURNED);

        if (intervention.getMechanic() != null) {
            Mechanic mechanic = intervention.getMechanic();
            mechanic.setAvailable(true);
            mechanicRepository.save(mechanic);
        }

        interventionRepository.save(intervention);

        InterventionHistory history = new InterventionHistory();
        history.setIntervention(intervention);
        history.setOldStatus(previousStatus);
        history.setNewStatus(Status.RETURNED);
        history.setComment("Vehicle returned to client");
        history.setAuthor(
                intervention.getMechanic() != null
                        ? intervention.getMechanic().getName()
                        : "SYSTEM");
        history.setDate(LocalDateTime.now());

        historyRepository.save(history);

        return intervention;
    }

    @Override
    public List<Intervention> getInterventionsByMechanic(Long mechanicId) {

        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() ->
                        new RuntimeException("Mechanic not found : " + mechanicId));

        return mechanic.getInterventions();
    }

    @Override
    public List<Intervention> getInterventionsByVehicle(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found : " + vehicleId));

        return vehicle.getInterventions();
    }

    @Override
    public List<Intervention> getDelayedInterventions() {
        LocalDateTime now = LocalDateTime.now();
        List<Status> excludedStatuses = List.of(Status.RETURNED, Status.CANCELLED);
        return interventionRepository.findDelayedInterventions(now, excludedStatuses);
    }


    @Override
    public List<Intervention> getOverdueInterventions() {

        return interventionRepository
                .findByEstimatedReturnDateBeforeAndStatusNot(
                        LocalDate.now().atStartOfDay(),
                        Status.RETURNED
                );
    }

    @Override
    public Double calculateTotalCost() {

        List<Intervention> interventions = interventionRepository.findAll();

        double sum = 0;

        for (Intervention intervention : interventions) {
            if (intervention.getEstimatedCost() != null) {
                sum += intervention.getEstimatedCost();
            }
        }

        return sum;
    }

    @Override
    public List<Intervention> getInterventionsByType(InterventionType type) {
        return interventionRepository.findInterventionByType(type);
    }

    @Override
    public List<Intervention> getInterventionsByPriority(Priority priority) {
        return interventionRepository.findByPriority(priority);
    }

}