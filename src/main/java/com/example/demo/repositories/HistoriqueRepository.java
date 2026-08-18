package com.example.demo.repositories;

import com.example.demo.entities.HistoriqueIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface HistoriqueRepository extends JpaRepository<HistoriqueIntervention , Long> {
    List<HistoriqueIntervention> findByInterventionIdOrderByDateDesc(Long interventionId);
}
