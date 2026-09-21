package com.example.demo.repositories;


import com.example.demo.entities.InterventionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface HistoryRepository extends JpaRepository<InterventionHistory , Long> {
    List<InterventionHistory> findByInterventionIdOrderByDateDesc(Long interventionId);
    boolean existsByInterventionId(Long interventionId);
    List<InterventionHistory> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
