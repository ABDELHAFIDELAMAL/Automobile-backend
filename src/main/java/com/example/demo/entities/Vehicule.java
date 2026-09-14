package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vehicules")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String immatriculation;

    @NotNull
    @Column(nullable = false)
    private String marque;

    @NotNull
    @Column(nullable = false)
    private String modele;

    @NotNull
    @Column(nullable = false)
    private Integer annee;

    @NotNull
    @Min(value = 0, message = "Le kilométrage ne peut pas être inférieur à 0")
    @Max(value = 99999999, message = "Le kilométrage maximal autorisé est de 99 999 999 km")
    @Column(nullable = false)
    private Integer kilometrage;

    @Column(nullable = false)
    private boolean clientFictif = false;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Intervention> interventions;
}