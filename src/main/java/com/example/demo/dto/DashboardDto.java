package com.example.demo.dto;

import com.example.demo.entities.Intervention;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
public class DashboardDto {

    private long recuesAujourdhui;
    private long enDiagnostic;
    private long enReparation;
    private long terminees;
    private Map<Long, Long> chargeParMecanicien;
    private List<Intervention> retardsRestitution;


    public DashboardDto(long recuesAujourdhui, long enDiagnostic, long enReparation, long terminees,
                             Map<Long, Long> chargeParMecanicien, List<Intervention> retardsRestitution) {
        this.recuesAujourdhui = recuesAujourdhui;
        this.enDiagnostic = enDiagnostic;
        this.enReparation = enReparation;
        this.terminees = terminees;
        this.chargeParMecanicien = chargeParMecanicien;
        this.retardsRestitution = retardsRestitution;
    }

}
