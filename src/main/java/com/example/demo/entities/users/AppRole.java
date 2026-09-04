package com.example.demo.entities.users;

import jakarta.persistence.*;
import lombok.*;
import com.example.demo.enums.Role;

import java.util.Collection;


@Entity
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppRole {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id ;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "roles")
    private Collection<Utilisateur> utilisateurs;

    public AppRole(Role role){
        this.role = role ;
    }

}
