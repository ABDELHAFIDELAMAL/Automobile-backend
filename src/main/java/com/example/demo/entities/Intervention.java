package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
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
    @JoinColumn(name = "mecanicien_id")
    private Mecanicien mecanicien;

    private BigDecimal coutEstime;

    private LocalDateTime dateDepot;

    private LocalDateTime dateRestitutionPrevue;

    private LocalDateTime dateCloture;

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL)
    private List<HistoriqueIntervention> historiqueInterventionList;

}