package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.entities.users.Role;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.repositories.users.UtilisateurRepository;
import com.example.demo.services.historique.HistoriqueService;
import com.example.demo.services.intervention.InterventionService;
import com.example.demo.services.mecanicien.MecanicienService;
import com.example.demo.services.user.UserService;
import com.example.demo.services.vehicule.VehiculeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    CommandLineRunner start(UserService userService , InterventionService interventionService, MecanicienService mecanicienService, VehiculeService vehiculeService, HistoriqueService historiqueService){
        return args -> {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.ajouterRole(Role.ADMIN);
            utilisateur.ajouterRole(Role.ADMIN);
            utilisateur.setNom("EL AMAL");
            utilisateur.setPrenom("Abdelhafid");
            utilisateur.setPassword("k10888");
            utilisateur.setEnabled(true);
            utilisateur.setEmail("abdelhafid.el-amal@capgemini.com");
            userService.createUtilisateur(utilisateur);

            Utilisateur utilisateur1 = new Utilisateur();
            utilisateur1.ajouterRole(Role.TECHNICIEN);
            utilisateur1.setNom("EL IROUI");
            utilisateur1.setPrenom("Chakib");
            utilisateur1.setPassword("k100000");
            utilisateur1.setEnabled(false);
            utilisateur1.setEmail("chakib.el-iroui@capgemini.com");
            userService.createUtilisateur(utilisateur1);


            Intervention intervention = new Intervention();
            intervention.setStatus(Status.RECUE);
            intervention.setDiagnostic("diagnostic N'0");
            intervention.setPriorite(Priorite.MOYENNE);
            intervention.setType(TypeIntervention.DIAGNOSTIC);
            intervention.setDateCloture(LocalDateTime.now());
            intervention.setCoutEstime(1500.00);
            intervention.setDateDepot(LocalDateTime.now());
            Mecanicien mecanicien = new Mecanicien();
            mecanicien.setSpecialite(Specialite.DIAGNOSTIC);
            mecanicien.setNom("mec001");
            mecanicien.setDisponible(true);
            mecanicienService.createMecanicien(mecanicien);
            intervention.setMecanicien(mecanicien);

            Vehicule vehicule = new Vehicule();
            vehicule.setImmatriculation("Maroc-01-A");
            vehicule.setKilometrage(2000);
            vehicule.setModele("M-2023");
            vehicule.setAnnee(2023);
            vehicule.setMarque("BMW");
            vehicule.setClientFictif(true);
            vehiculeService.createVehicule(vehicule);

            intervention.setVehicule(vehicule);
            intervention.setDescription("vehicle bmw m4 cs");
            intervention.setDateRestitutionPrevue(LocalDateTime.now());
            intervention.setDateRestitutionPrevue(LocalDateTime.MAX);

            Intervention savedIntervention =
                    interventionService.createIntervention(intervention);

            HistoriqueIntervention hist = new HistoriqueIntervention();
            hist.setCommentaire("Diagnostic at : " + LocalDateTime.now());
            hist.setDate(LocalDateTime.now());
            hist.setAuteur("Mohsin EL AMAL");

            hist.setIntervention(savedIntervention);
            hist.setAncienStatus(savedIntervention.getStatus());
            hist.setNouveauStatus(Status.DIAGNOSTIC_EN_COURS);

            historiqueService.createHistorique(hist);




        };
    }
}
