package com.example.demo.auth;

import com.example.demo.enums.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.jwt.JwtService;
import com.example.demo.repositories.users.UtilisateurRepository;
import com.example.demo.request.LoginRequest;
import com.example.demo.request.RegisterRequest;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;


@Service
// cette Annotation generate constructor pour les finals
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Transactional
    public Map<String, String> register(RegisterRequest request) throws EmailAlreadyExistsException {

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

        String jwt = jwtService.generateToken(request.getEmail());
        return Map.of("Access-Token", jwt);
    }

    public Map<String , String> login(LoginRequest request) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String jwt = jwtService.generateToken(request.getEmail());
        return Map.of("Access-Token", jwt);
    }


}
