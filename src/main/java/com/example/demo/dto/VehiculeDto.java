package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculeDto {
    private Long id;

    @NotNull(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    @NotNull(message = "La marque est obligatoire")
    private String marque;

    @NotNull(message = "Le modèle est obligatoire")
    private String modele;

    @NotNull(message = "L'année est obligatoire")
    private Integer annee;

    @NotNull(message = "Le kilométrage est obligatoire")
    @Positive(message = "Le kilométrage doit être positif")
    private Integer kilometrage;

    private boolean clientFictif;

}
