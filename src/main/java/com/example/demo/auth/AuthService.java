package com.example.demo.auth;

import com.example.demo.enums.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.jwt.JwtUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = jwtUtils.generateToken(authentication);
        return Map.of("Access-Token", jwt);
    }

    public Map<String, String> login(LoginRequest request) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String jwt = jwtUtils.generateToken(authentication);
        return Map.of("Access-Token", jwt);
    }
}
