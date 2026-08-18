package com.example.demo.entities;

import jakarta.persistence.*;
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
    @Positive(message = "Le kilométrage doit être positif")
    @Column(nullable = false)
    private Integer kilometrage;

    @Column(nullable = false)
    private boolean clientFictif = false;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Intervention> interventions;
}