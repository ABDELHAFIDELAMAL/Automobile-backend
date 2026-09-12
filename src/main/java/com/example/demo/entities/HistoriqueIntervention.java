package com.example.demo.entities;

import com.example.demo.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_interventions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueIntervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "intervention_id")
    @JsonIgnore
    private Intervention intervention;

    @Enumerated(EnumType.STRING)
    private Status ancienStatus;

    @Enumerated(EnumType.STRING)
    private Status nouveauStatus;

    @Size(min = 3, max = 100)
    private String commentaire;

    private LocalDateTime date;

    private String auteur;
}