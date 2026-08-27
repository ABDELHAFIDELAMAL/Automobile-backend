package com.example.demo.auth;

import com.example.demo.entities.users.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.jwt.JwtService;
import com.example.demo.repositories.users.UtilisateurRepository;
import com.example.demo.request.RegisterRequest;
import com.example.demo.exceptions.EmailAlreadyExistsException; // Exception personnalisée
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
// cette Annotation generate constructor pour les finals
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) throws EmailAlreadyExistsException {

        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Cet email est deja utilise.");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        utilisateur.ajouterRole(Role.USER);

        utilisateurRepository.save(utilisateur);

        String token = jwtService.generateToken(utilisateur.getEmail());

        return new AuthResponse(token);
    }
}
