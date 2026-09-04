package com.example.demo.controllers.users;

import com.example.demo.enums.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.services.user.IUserService;
import com.example.demo.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/users")
@CrossOrigin("*")
public class UtilisateurController {
    private final IUserService userService;

    public UtilisateurController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        return getAllUsers();
    }

    @GetMapping(path = "/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<Utilisateur> data = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse("Users fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch users", null, false));
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse> getUtilisateurById(@PathVariable Long id) {
        try {
            Utilisateur data = userService.getUtilisateurById(id);
            return ResponseEntity.ok(new ApiResponse("User fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("User not found", null, false));
        }
    }

    @GetMapping(path = "/username")
    public ResponseEntity<ApiResponse> getUtilisateurByUsername(@RequestParam String username) {
        try {
            Optional<Utilisateur> data = userService.getUtilisateurByUsername(username);
            if (data.isPresent()) {
                return ResponseEntity.ok(new ApiResponse("User fetched successfully", data.get(), true));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("User not found with this username", null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error fetching user", null, false));
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<ApiResponse> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur data = userService.createUtilisateur(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("User created successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to create user", null, false));
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ApiResponse> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur data = userService.updateUtilisateur(id, utilisateur);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to update user", null, false));
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUtilisatuer(@PathVariable Long id) {
        try {
            userService.deleteUtilisatuer(id);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Failed to delete user", null, false));
        }
    }

    @PatchMapping(path = "/activer/{id}")
    public ResponseEntity<ApiResponse> activer(@PathVariable Long id) {
        try {
            Utilisateur data = userService.activer(id);
            return ResponseEntity.ok(new ApiResponse("User activated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to activate user", null, false));
        }
    }

    @PatchMapping(path = "/desactiver/{id}")
    public ResponseEntity<ApiResponse> desactiver(@PathVariable Long id) {
        try {
            Utilisateur data = userService.desactiver(id);
            return ResponseEntity.ok(new ApiResponse("User deactivated successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to deactivate user", null, false));
        }
    }

    @PatchMapping("/change/{id}")
    public ResponseEntity<ApiResponse> changerMotePass(@PathVariable Long id,
                                                       @RequestParam String ancienMdps, @RequestParam String nouveauMdps) {
        try {
            Utilisateur data = userService.changerMotdePass(id, ancienMdps, nouveauMdps);
            return ResponseEntity.ok(new ApiResponse("Password changed successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to change password", null, false));
        }
    }

    @PutMapping(path = "/addrole/{id}")
    public ResponseEntity<ApiResponse> affecterRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            userService.affecterRole(id, role);
            return ResponseEntity.ok(new ApiResponse("Role assigned successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Failed to assign role", null, false));
        }
    }
}
