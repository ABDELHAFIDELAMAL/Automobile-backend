package com.example.demo.controllers;

import com.example.demo.dto.UserDto;
import com.example.demo.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final WebClient webClient = WebClient.create();
    private final String keycloakUrl = "http://localhost:8080";
    private final String realm = "AutomobileRealm";
    private final String clientId = "AutomobileClient";
    private final String clientSecret = "IzP9XPRzTByiclsEugKJeFN94ypPf7uziR62ijj1HeJnl0i9KcT5Em7RkqKXL30pvGm3AivRv1dPBupbRBmMBo";

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserDto dto) {
        if (!dto.isTerms()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("You must accept the terms and conditions", null, false));
        }

        try {
            String adminToken = fetchAdminToken();

            Map<String, Object> passwordCredential = Map.of(
                    "type", "password",
                    "value", dto.getPassword(),
                    "temporary", false
            );

            Map<String, Object> keycloakRepresentation = Map.of(
                    "username", dto.getUsername(),
                    "email", dto.getEmail(),
                    "enabled", true,
                    "emailVerified", true,
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

            dto.setPassword(null);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Registration successful", dto, true));

        } catch (WebClientResponseException e) {
            String errorMessage = "Keycloak error: " + e.getResponseBodyAsString();
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse(errorMessage, null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null, false));
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

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Failed to retrieve admin token from Keycloak");
        }

        return (String) response.get("access_token");
    }
}
