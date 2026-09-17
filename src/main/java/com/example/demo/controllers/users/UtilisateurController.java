package com.example.demo.controllers.users;

import com.example.demo.entities.users.Utilisateur;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.AllReadyExistException;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.user.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/utilisateurs")
@CrossOrigin(origins = "*")
public class UtilisateurController {
    private final IUserService userService;

    public UtilisateurController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<Utilisateur> users = userService.getAllUsers();
        ApiResponse response = new ApiResponse("Liste des utilisateurs récupérée avec succès", users, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> getUtilisateurById(@PathVariable Long id) {
        Utilisateur user = userService.getUtilisateurById(id);
        ApiResponse response = new ApiResponse("Utilisateur trouvé avec succès", user, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/username")
    public ResponseEntity<ApiResponse> getUtilisateurByUsername(@RequestParam String username) {
        Optional<Utilisateur> user = userService.getUtilisateurByUsername(username);
        ApiResponse response = new ApiResponse("Recherche par nom d'utilisateur terminée", user, true);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<ApiResponse> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur createdUser = userService.createUtilisateur(utilisateur);
            ApiResponse response = new ApiResponse("Utilisateur créé avec succès", createdUser, true);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AllReadyExistException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        Utilisateur updatedUser = userService.updateUtilisateur(id, utilisateur);
        ApiResponse response = new ApiResponse("Utilisateur mis à jour avec succès", updatedUser, true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUtilisateur(@PathVariable Long id) {
        userService.deleteUtilisatuer(id);
        ApiResponse response = new ApiResponse("Utilisateur supprimé avec succès", null, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/activer/{id}")
    public ResponseEntity<ApiResponse> activer(@PathVariable Long id) {
        Utilisateur user = userService.activer(id);
        ApiResponse response = new ApiResponse("Utilisateur activé avec succès", user, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/desactiver/{id}")
    public ResponseEntity<ApiResponse> desactiver(@PathVariable Long id) {
        Utilisateur user = userService.desactiver(id);
        ApiResponse response = new ApiResponse("Utilisateur désactivé avec succès", user, true);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change/{id}")
    public ResponseEntity<ApiResponse> changerMotdePass(@PathVariable Long id,
                                                        @RequestParam String ancienMdps,
                                                        @RequestParam String nouveauMdps) {
        Utilisateur user = userService.changerMotdePass(id, ancienMdps, nouveauMdps);
        ApiResponse response = new ApiResponse("Mot de passe modifié avec succès", user, true);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/addrole/{id}")
    public ResponseEntity<ApiResponse> affecterRole(@PathVariable Long id, @RequestBody Role role) {
        userService.affecterRole(id, role);
        ApiResponse response = new ApiResponse("Rôle affecté avec succès", null, true);
        return ResponseEntity.ok(response);
    }
}
