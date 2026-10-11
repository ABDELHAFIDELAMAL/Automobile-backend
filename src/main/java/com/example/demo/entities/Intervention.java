package com.example.demo.entities;

import com.example.demo.enums.Priority;
import com.example.demo.enums.Status;
import com.example.demo.enums.InterventionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "interventions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InterventionType type;

    private String description;

    private String diagnostic;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;

    private Double estimatedCost;

    private LocalDateTime depositDate;

    private LocalDateTime estimatedReturnDate;

    private LocalDateTime closureDate;

    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL)
    private List<InterventionHistory> interventionHistoryList;

}
