package com.example.demo.dto;

import com.example.demo.enums.Specialite;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MecanicienDto {

    private Long id;

    @NotNull(message = "Le nom est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre {min} et {max} caractères")
    private String nom;

    @NotNull(message = "La spécialité est obligatoire")
    private Specialite specialite;

    private boolean disponible;

}
