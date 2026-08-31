package com.example.demo.repositories.users;

import com.example.demo.entities.users.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findUtilisateurByNom(String nom);
    Optional<Utilisateur> findByEmail(String email);

}
