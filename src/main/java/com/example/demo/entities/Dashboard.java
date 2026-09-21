package com.example.demo.entities;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Dashboard {

    private long receivedToday;
    private long inProgressDiagnostic;
    private long underRepair;
    private long completed;
    private Map<Long, Long> workloadPerMechanic;
    private List<Intervention> delayedReturns;

    public Dashboard(long receivedToday, long inProgressDiagnostic, long underRepair, long completed,
                        Map<Long, Long> workloadPerMechanic, List<Intervention> delayedReturns) {
        this.receivedToday = receivedToday;
        this.inProgressDiagnostic = inProgressDiagnostic;
        this.underRepair = underRepair;
        this.completed = completed;
        this.workloadPerMechanic = workloadPerMechanic;
        this.delayedReturns = delayedReturns;
    }
}
