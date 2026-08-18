package com.example.demo;

import com.example.demo.entities.users.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.repositories.users.UtilisateurRepository;
import com.example.demo.services.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AutomobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomobileApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner start(UserService userService){
        return args -> {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.ajouterRole(Role.ADMIN);
            utilisateur.setNom("EL AMAL");
            utilisateur.setPrenom("Abdelhafid");
            utilisateur.setPassword("k13030980");
            utilisateur.setEnabled(true);
            utilisateur.setEmail("abdelhafid.el-amal@capgemini.com");
            userService.createUtilisateur(utilisateur);

            for (Utilisateur user : userService.getAllUsers()){
                System.out.println(user.toString());
            }
        };
    }
}
