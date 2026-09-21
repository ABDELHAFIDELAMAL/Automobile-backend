package com.example.demo.entities;

import com.example.demo.enums.Specialty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "mechanics")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mechanic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    @Column(nullable = false)
    private boolean available = true;

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Intervention> interventions;

}
