package com.example.demo.controllers.users;

import com.example.demo.entities.users.Utilisateur;
import com.example.demo.services.user.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UtilisateurController {
    private final IUserService userService ;

    public UtilisateurController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Utilisateur> getAllUsers(){
        return userService.getAllUsers();
    }
}
