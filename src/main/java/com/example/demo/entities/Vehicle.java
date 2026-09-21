package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String matricule;

    @NotNull
    @Column(nullable = false)
    private String make;

    @NotNull
    @Column(nullable = false)
    private String model;

    @NotNull
    @Column(nullable = false , name = "vehicle_year")
    private Integer year;

    @NotNull
    @Min(value = 0, message = "Mileage cannot be less than 0")
    @Max(value = 99999999, message = "Maximum allowed mileage is 99,999,999 km")
    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    private boolean dummyClient = false;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Intervention> interventions;
}
