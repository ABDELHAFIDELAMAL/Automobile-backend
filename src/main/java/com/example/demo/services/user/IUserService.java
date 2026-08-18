package com.example.demo.services.user;

import com.example.demo.entities.users.Role;
import com.example.demo.entities.users.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<Utilisateur> getAllUsers();
    Utilisateur getUtilisateurById(Long id);
    Optional<Utilisateur> getUtilisateurByUsername(String username);
    Utilisateur createUtilisateur(Utilisateur utilisateur);
    Utilisateur updateUtilisateur(Long id , Utilisateur utilisateur);
    void deleteUtilisatuer(Long id);
    Utilisateur activer(Long id);
    Utilisateur desactiver(Long id);
    Utilisateur changerMotdePass(Long id , String ancienMdps , String nouveauMdps);
    void affecterRole(Long id , Role role);

}
