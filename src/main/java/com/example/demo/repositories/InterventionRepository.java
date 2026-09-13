package com.example.demo.repositories;

import com.example.demo.entities.Intervention;
import com.example.demo.entities.Mecanicien;
import com.example.demo.enums.Status;
import com.example.demo.enums.TypeIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterventionRepository
    extends JpaRepository<Intervention , Long> {

    List<Intervention> findInterventionsByMecanicien(Mecanicien mecanicien);

    List<Intervention> findByDateRestitutionPrevueBeforeAndStatusNot(LocalDateTime dateRestitutionPrevue, Status status);

    List<Intervention> findInterventionByType(TypeIntervention type);

    long countByDateDepotBetween(LocalDateTime debutAujourdhui, LocalDateTime finAujourdhui);

    long countByStatus(Status status);

    List<Intervention> findByStatus(Status status);

    @Query("SELECT i FROM Intervention i WHERE i.dateRestitutionPrevue < :maintenant AND i.status NOT IN :statusExclus")
    List<Intervention> findRetards(@Param("maintenant") LocalDateTime maintenant, @Param("statusExclus") List<Status> statusExclus);

    boolean existsByVehiculeIdAndTypeAndDescription(Long vehiculeId, TypeIntervention type, String description);

}