package com.example.demo.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String nom;

    @NotBlank
    @Size(min = 3, max = 50)
    private String prenom;

    @Email
    @Column(unique = true)
    private String email;

    @Size(min = 6, max = 100)
    @JsonIgnore
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "utilisateur_roles",
            joinColumns = @JoinColumn(name = "utilisateur_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    private boolean enabled = true;

    public void ajouterRole(Role role) {
        this.roles.add(role);
    }
}
