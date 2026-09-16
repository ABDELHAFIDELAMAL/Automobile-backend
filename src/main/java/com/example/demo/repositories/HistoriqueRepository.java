package com.example.demo.repositories;

import com.example.demo.entities.HistoriqueIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface HistoriqueRepository extends JpaRepository<HistoriqueIntervention , Long> {
    List<HistoriqueIntervention> findByInterventionIdOrderByDateDesc(Long interventionId);

    boolean existsByInterventionId(Long interventionId);

    List<HistoriqueIntervention> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
