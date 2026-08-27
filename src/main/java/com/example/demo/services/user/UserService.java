package com.example.demo.services.user;

import com.example.demo.entities.users.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.repositories.users.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {


    private final UtilisateurRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public UserService(UtilisateurRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Utilisateur> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Utilisateur getUtilisateurById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur n'existe pas avec l'id = " + id));
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByUsername(String username) {
        return userRepository.findUtilisateurByNom(username);
    }

    @Override
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setPassword(
                passwordEncoder.encode(utilisateur.getPassword())
        );
        return userRepository.save(utilisateur);
    }

    @Override
    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateur) {
        Utilisateur user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur n'existe pas avec l'id = " + id));

        user.setNom(utilisateur.getNom());
        user.setPrenom(utilisateur.getPrenom());
        user.setEmail(utilisateur.getEmail());
        user.setRoles(utilisateur.getRoles());

        if (utilisateur.getPassword() != null && !utilisateur.getPassword().isEmpty()) {
            user.setPassword(
                    passwordEncoder.encode(utilisateur.getPassword())
            );
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUtilisatuer(Long id) {
        Utilisateur user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur n'existe pas avec l'id = " + id));

        userRepository.delete(user);
    }

    @Override
    public Utilisateur activer(Long id) {
        Utilisateur user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur n'existe pas avec l'id = " + id));

        if (user.isEnabled()) {
            throw new IllegalStateException("Utilisateur déjà activé");
        }

        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public Utilisateur desactiver(Long id) {
        Utilisateur user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur n'existe pas avec l'id = " + id));

        if (!user.isEnabled()) {
            throw new IllegalStateException("Utilisateur déjà désactivé");
        }

        user.setEnabled(false);
        return userRepository.save(user);
    }

    @Override
    public Utilisateur changerMotdePass(Long id, String ancienMdps, String nouveauMdps) {
        Utilisateur user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur n'existe pas avec l'id = " + id));

        if (!passwordEncoder.matches(ancienMdps, user.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        user.setPassword(passwordEncoder.encode(nouveauMdps));

        return userRepository.save(user);
    }

    @Override
    public void affecterRole(Long id, Role role) {
        Utilisateur user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur nExiste pas avec id = " + id));

        user.ajouterRole(role);

        userRepository.save(user);
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}