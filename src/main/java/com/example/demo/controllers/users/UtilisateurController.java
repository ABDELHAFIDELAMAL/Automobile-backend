package com.example.demo.controllers.users;

import com.example.demo.entities.users.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.services.user.IUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UtilisateurController {
    private final IUserService userService ;

    public UtilisateurController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Utilisateur> getAll(){
        return getAllUsers();
    }
    @GetMapping(path = "/all")
    public List<Utilisateur> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping(path = "/{id}")
    public Utilisateur getUtilisateurById(@PathVariable Long id){
        return userService.getUtilisateurById(id);
    }

    @GetMapping(path = "/username")
    public Optional<Utilisateur> getUtilisateurByUsername(@RequestParam String username) {
        return userService.getUtilisateurByUsername(username);
    }

    @PostMapping(path = "/add")
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return userService.createUtilisateur(utilisateur);
    }

    @PutMapping(path = "/update/{id}")
    public Utilisateur updateUtilisateur(@PathVariable Long id,@RequestBody Utilisateur utilisateur) {
        return userService.updateUtilisateur(id , utilisateur);
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteUtilisatuer(@PathVariable Long id) {
        userService.deleteUtilisatuer(id);
    }
    @PatchMapping(path = "/activer/{id}")
    public Utilisateur activer(@PathVariable Long id) {
        return userService.activer(id);
    }

    @PatchMapping(path = "/desactiver/{id}")
    public Utilisateur desactiver(@PathVariable Long id) {
        return userService.desactiver(id);
    }

    @PatchMapping("/change/{id}")
    public Utilisateur changerMotdePass(@PathVariable Long id,
            @RequestParam String ancienMdps, @RequestParam String nouveauMdps) {
        return userService.changerMotdePass(id, ancienMdps, nouveauMdps);
    }


    @PutMapping(path = "/addrole/{id}")
    public void affecterRole(@PathVariable Long id,@RequestBody Role role) {
        userService.affecterRole(id , role);
    }
}
