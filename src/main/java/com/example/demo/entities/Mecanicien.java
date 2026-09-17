package com.example.demo.entities;

import com.example.demo.enums.Specialite;
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

    @OneToMany(mappedBy = "mecanicien", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Intervention> interventions;

}