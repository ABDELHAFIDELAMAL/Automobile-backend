package com.example.demo.dto;

import com.example.demo.entities.Intervention;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
public class DashboardDto {

    private long reçuesAujourdhui;
    private long enDiagnostic;
    private long enRéparation;
    private long terminées;
    private Map<Long, Long> chargeParMécanicien;
    private List<Intervention> retardsRestitution;


    public DashboardDto(long reçuesAujourdhui, long enDiagnostic, long enRéparation, long terminées,
                             Map<Long, Long> chargeParMécanicien, List<Intervention> retardsRestitution) {
        this.reçuesAujourdhui = reçuesAujourdhui;
        this.enDiagnostic = enDiagnostic;
        this.enRéparation = enRéparation;
        this.terminées = terminées;
        this.chargeParMécanicien = chargeParMécanicien;
        this.retardsRestitution = retardsRestitution;
    }

}
