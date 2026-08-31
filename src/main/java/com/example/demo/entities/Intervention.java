package com.example.demo.entities;

import com.example.demo.enums.Priorite;
import com.example.demo.enums.Status;
import com.example.demo.enums.TypeIntervention;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interventions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeIntervention type;

    private String description;

    private String diagnostic;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priorite priorite;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "mecanicien_id")
    private Mecanicien mecanicien;

    private Double coutEstime;

    private LocalDateTime dateDepot;

    private LocalDateTime dateRestitutionPrevue;

    private LocalDateTime dateCloture;

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL)
    private List<HistoriqueIntervention> historiqueInterventionList;

}