package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "mecaniciens")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mecanicien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialite specialite;

    @Column(nullable = false)
    private boolean disponible = true;

    @OneToMany(mappedBy = "mecanicien", fetch = FetchType.LAZY)
    private List<Intervention> interventions;
}