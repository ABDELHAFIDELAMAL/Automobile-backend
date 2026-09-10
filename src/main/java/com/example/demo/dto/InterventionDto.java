package com.example.demo.dto;

import com.example.demo.enums.Priorite;
import com.example.demo.enums.Status;
import com.example.demo.enums.TypeIntervention;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterventionDto {
    private Long id;
    private Long vehiculeId;

    @NotNull(message = "Le type d'intervention est obligatoire")
    private TypeIntervention type;
    private String description;
    private String diagnostic;
    private Status status;
    private Priorite priorite;
    private Long mecanicienId;
    private Double coutEstime;
    private LocalDateTime dateDepot;
    private LocalDateTime dateRestitutionPrevue;
    private LocalDateTime dateCloture;

}
