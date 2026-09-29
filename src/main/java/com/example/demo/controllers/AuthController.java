package com.example.demo.controllers;

import com.example.demo.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private PasswordEncoder passwordEncoder;

    private final WebClient webClient = WebClient.create();

    private final String keycloakUrl = "http://localhost:8080";
    private final String realm = "AutomobileRealm";
    private final String clientId = "AutomobileClient";
    private final String clientSecret = "2iiaDQghtJdyrryWrlBwyTePRKwDNdQ1f0xgbOncxZ3NKkLPVNCwGSxurZGNBDtV8FB70FDx7QmJkjS6buebYP";

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto dto) {
        try {
            String adminToken = fetchAdminToken();

            Map<String, Object> passwordCredential = Map.of(
                    "type", "password",
                    "value", passwordEncoder.encode(dto.getPassword()) ,
                    "temporary", false
            );

            Map<String, Object> keycloakRepresentation = Map.of(
                    "username", dto.getUsername(),
                    "email", dto.getEmail(),
                    "enabled", true,
                    "credentials", List.of(passwordCredential)
            );

            webClient.post()
                    .uri(keycloakUrl + "/admin/realms/" + realm + "/users")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(keycloakRepresentation)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Registration successful"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    private String fetchAdminToken() {
        Map<?, ?> response = webClient.post()
                .uri(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (String) response.get("access_token");
    }
}
