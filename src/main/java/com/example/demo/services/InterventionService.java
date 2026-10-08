package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.enums.InterventionType;
import com.example.demo.enums.Priority;
import com.example.demo.enums.Status;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.repositories.HistoryRepository;
import com.example.demo.repositories.InterventionRepository;
import com.example.demo.repositories.MechanicRepository;
import com.example.demo.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private Intervention findOrThrow(Long id) {
        return interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found with id : " + id));
    }

    private String authorOf(Intervention intervention) {
        return intervention.getMechanic() != null
                ? intervention.getMechanic().getName()
                : "SYSTEM";
    }

    private void saveHistory(Intervention intervention, Status oldStatus, Status newStatus,
                             String comment, String author) {
        InterventionHistory history = new InterventionHistory();
        history.setIntervention(intervention);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setComment(comment);
        history.setAuthor(author);
        history.setDate(LocalDateTime.now());
        historyRepository.save(history);
    }

    private void releaseMechanic(Intervention intervention) {
        Mechanic mechanic = intervention.getMechanic();
        if (mechanic != null) {
            mechanic.setAvailable(true);
            mechanicRepository.save(mechanic);
        }
    }


    @Override
    @Transactional
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

    @Override
    @Transactional
    public Intervention getInterventionById(Long id) {
        return findOrThrow(id);
    }


    @Override
    @Transactional
    public List<Intervention> getDelayedInterventions() {
        List<Status> excludedStatuses = List.of(RETURNED, CANCELLED);
        return interventionRepository.findDelayedInterventions(LocalDateTime.now(), excludedStatuses);
    }

    @Override
    @Transactional
    public List<Intervention> getOverdueInterventions() {
        return interventionRepository.findByEstimatedReturnDateBeforeAndStatusNot(
                LocalDateTime.now().toLocalDate().atStartOfDay(),
                RETURNED
        );
    }

    @Override
    @Transactional
    public Double calculateTotalCost() {
        return interventionRepository.findAll().stream()
                .filter(i -> i.getEstimatedCost() != null)
                .mapToDouble(Intervention::getEstimatedCost)
                .sum();
    }

    @Override
    @Transactional
    public List<Intervention> getInterventionsByType(InterventionType type) {
        return interventionRepository.findInterventionByType(type);
    }

    @Override
    @Transactional
    public List<Intervention> getInterventionsByPriority(Priority priority) {
        return interventionRepository.findByPriority(priority);
    }

    @Override
    public void cancelIntervention(Long id) {
        Intervention intervention = findOrThrow(id);
        if(intervention.getMechanic() != null) {
            intervention.getMechanic().setAvailable(true);
        }
        interventionRepository.delete(intervention);
    }

    @Override
    public void cancelAllInterventions() {
        for(Intervention intervention : interventionRepository.findAll()){
            if (intervention.getMechanic() != null) {
                intervention.getMechanic().setAvailable(true);
            }
        }
        this.interventionRepository.deleteAll();
    }

    @Override
    public Intervention createIntervention(Intervention intervention) {

        if (intervention.getVehicle() == null || intervention.getVehicle().getId() == null) {
            throw new IllegalArgumentException("A vehicle is required to create an intervention");
        }

        boolean exist = interventionRepository.existsByVehicleIdAndTypeAndDescription(
                intervention.getVehicle().getId(),
                intervention.getType(),
                intervention.getDescription()
        );

        if (exist) {
            throw new AllReadyExistException(
                    "Intervention already exists for vehicle id " + intervention.getVehicle().getId());
        }

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention updateIntervention(Long id, Intervention intervention) {

        Intervention interv = findOrThrow(id);

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

        Intervention intervention = findOrThrow(id);

        Mechanic found = mechanicRepository.findById(mechanic.getId())
                .orElseThrow(() -> new RuntimeException("Mechanic not found with id : " + mechanic.getId()));

        if (!found.isAvailable()) {
            throw new IllegalStateException("This mechanic is currently not available.");
        }

        intervention.setMechanic(found);

        if (intervention.getStatus() == QUOTATION_TO_VALIDATE) {
            intervention.setStatus(UNDER_REPAIR);
        }

        found.setAvailable(false);
        mechanicRepository.save(found);

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention setEstimatedCost(Long id, Double cost) {

        if (cost == null || cost <= 0) {
            throw new IllegalArgumentException("Estimated cost must be greater than 0");
        }

        Intervention intervention = findOrThrow(id);

        intervention.setEstimatedCost(cost);

        if (intervention.getStatus() == DIAGNOSTIC_IN_PROGRESS) {
            intervention.setStatus(QUOTATION_TO_VALIDATE);
        }

        return interventionRepository.save(intervention);
    }

    @Override
    public Intervention addDiagnostic(Long id, String diagnostic) {

        Intervention intervention = findOrThrow(id);

        if (intervention.getStatus() != RECEIVED) {
            throw new IllegalStateException("Cannot add diagnostic: intervention must be RECEIVED");
        }

        Status previousStatus = intervention.getStatus();

        intervention.setDiagnostic(diagnostic);
        intervention.setStatus(DIAGNOSTIC_IN_PROGRESS);

        Intervention saved = interventionRepository.save(intervention);

        saveHistory(saved, previousStatus, DIAGNOSTIC_IN_PROGRESS,
                "Technical diagnostic added by mechanic.", authorOf(saved));

        return saved;
    }

    @Override
    public Intervention changeStatus(Long id, Status newStatus, String author) {

        Intervention intervention = findOrThrow(id);
        Status currentStatus = intervention.getStatus();

        boolean valid = (currentStatus == newStatus) || (newStatus == Status.CANCELLED) || switch (currentStatus) {
            case RECEIVED -> newStatus == Status.DIAGNOSTIC_IN_PROGRESS;
            case DIAGNOSTIC_IN_PROGRESS -> newStatus == Status.QUOTATION_TO_VALIDATE;
            case QUOTATION_TO_VALIDATE -> newStatus == Status.UNDER_REPAIR;
            case UNDER_REPAIR -> newStatus == Status.COMPLETED;
            case COMPLETED -> newStatus == Status.RETURNED;
            default -> false;
        };


        if (!valid) {
            throw new IllegalStateException(
                    "Transition from " + currentStatus + " to " + newStatus + " forbidden.");
        }

        // Rules :
        if (newStatus == QUOTATION_TO_VALIDATE) {
            if (intervention.getEstimatedCost() == null || intervention.getEstimatedCost() <= 0) {
                throw new IllegalArgumentException(
                        "RG-AUTO-05: Estimated cost is required before switching to Quotation to Validate status.");
            }
        }

        if (newStatus == UNDER_REPAIR) {
            if (intervention.getMechanic() == null || intervention.getMechanic().getId() == null) {
                throw new IllegalArgumentException(
                        "RG-AUTO-06: A mechanic must be assigned before switching to Under Repair status.");
            }
        }

        if (newStatus == RETURNED) {
            intervention.setClosureDate(LocalDateTime.now());
            releaseMechanic(intervention);
        }

        intervention.setStatus(newStatus);
        Intervention saved = interventionRepository.save(intervention);

        saveHistory(saved, currentStatus, newStatus, null, author);

        return saved;
    }

    @Override
    public Intervention complete(Long id) {

        Intervention intervention = findOrThrow(id);

        if (intervention.getStatus() != UNDER_REPAIR) {
            throw new IllegalStateException("Intervention must be UNDER_REPAIR");
        }

        Status previousStatus = intervention.getStatus();

        intervention.setStatus(COMPLETED);
        intervention.setClosureDate(LocalDateTime.now());

        Intervention saved = interventionRepository.save(intervention);

        saveHistory(saved, previousStatus, COMPLETED,
                "Intervention completed", authorOf(saved));

        return saved;
    }

    @Override
    public Intervention returnIntervention(Long id) {

        Intervention intervention = findOrThrow(id);

        if (intervention.getStatus() != COMPLETED) {
            throw new IllegalStateException("The intervention must be COMPLETED before return");
        }

        Status oldStatus = intervention.getStatus();
        String author = authorOf(intervention);

        intervention.setStatus(RETURNED);
        intervention.setClosureDate(LocalDateTime.now());
        releaseMechanic(intervention);

        Intervention saved = interventionRepository.save(intervention);

        saveHistory(saved, oldStatus, RETURNED,
                "Vehicle returned to the client and mechanic released.", author);

        return saved;
    }

    @Override
    public Intervention returnVehicle(Long id) {
        return returnIntervention(id);
    }
}