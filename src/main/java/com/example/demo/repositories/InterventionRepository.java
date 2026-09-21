package com.example.demo.repositories;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mechanic;
import com.example.demo.enums.Priority;
import com.example.demo.enums.Status;
import com.example.demo.enums.InterventionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterventionRepository
        extends JpaRepository<Intervention, Long> {

    List<Intervention> findInterventionsByMechanic(Mechanic mechanic);

    List<Intervention> findInterventionByType(InterventionType type);

    List<Intervention> findByEstimatedReturnDateBeforeAndStatusNot(LocalDateTime date, Status status);

    long countByDepositDateBetween(LocalDateTime startOfToday, LocalDateTime endOfToday);

    long countByStatus(Status status);

    List<Intervention> findByStatus(Status status);

    @Query("SELECT i FROM Intervention i WHERE i.estimatedReturnDate < :now AND i.status NOT IN :excludedStatus")
    List<Intervention> findDelayedInterventions(@Param("now") LocalDateTime now, @Param("excludedStatus") List<Status> excludedStatus);

    boolean existsByVehicleIdAndTypeAndDescription(Long vehicleId, InterventionType type, String description);

    List<Intervention> findByPriority(Priority priority);

}
