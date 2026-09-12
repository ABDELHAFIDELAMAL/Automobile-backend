package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.entities.users.Utilisateur;
import com.example.demo.enums.*;
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
            utilisateur.ajouterRole(Role.CONSEILLER);
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
            intervention.setDiagnostic("diagnostic Numero 001");
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

            Utilisateur conseiller = new Utilisateur();
            conseiller.ajouterRole(Role.CONSEILLER);
            conseiller.setNom("BENNANI");
            conseiller.setPrenom("Youssef");
            conseiller.setPassword("at1020");
            conseiller.setEnabled(true);
            conseiller.setEmail("youssef.bennani@capgemini.com");
            userService.createUtilisateur(conseiller);

            Utilisateur manager = new Utilisateur();
            manager.ajouterRole(Role.MANAGER); // Ou Role.MANAGER selon votre Enum
            manager.setNom("ALAMI");
            manager.setPrenom("Amine");
            manager.setPassword("mgr3000");
            manager.setEnabled(true);
            manager.setEmail("amine.alami@capgemini.com");
            userService.createUtilisateur(manager);


            // ==========================================
            // 2. ÉQUIPE DE MÉCANICIENS (SPÉCIALITÉS COMPLÈTES)
            // ==========================================

            // Mécanicien 1 : Électricité / Électronique (Disponible)
            Mecanicien mecElec = new Mecanicien();
            mecElec.setSpecialite(Specialite.ELECTRICITE_ELECTRONIQUE);
            mecElec.setNom("Hassan Bouras");
            mecElec.setDisponible(true);
            mecanicienService.createMecanicien(mecElec);


            Mecanicien mecPneu = new Mecanicien();
            mecPneu.setSpecialite(Specialite.PNEUMATIQUE);
            mecPneu.setNom("Karim Tazi");
            mecPneu.setDisponible(false);
            mecanicienService.createMecanicien(mecPneu);


            Vehicule vReel = new Vehicule();
            vReel.setImmatriculation("Maroc-99-B-1234");
            vReel.setKilometrage(85000);
            vReel.setModele("Golf 7");
            vReel.setAnnee(2019);
            vReel.setMarque("Volkswagen");
            vReel.setClientFictif(false);
            vehiculeService.createVehicule(vReel);

            Vehicule vFictif = new Vehicule();
            vFictif.setImmatriculation("Maroc-77-X-5678");
            vFictif.setKilometrage(12000);
            vFictif.setModele("Clio 5");
            vFictif.setAnnee(2021);
            vFictif.setMarque("Renault");
            vFictif.setClientFictif(true);
            vehiculeService.createVehicule(vFictif);


            Intervention intRetard = new Intervention();
            intRetard.setStatus(Status.EN_REPARATION);
            intRetard.setPriorite(Priorite.HAUTE);
            intRetard.setType(TypeIntervention.REPARATION);
            intRetard.setDescription("Changement d'embrayage complet");
            intRetard.setDiagnostic("Butée d'embrayage totalement détruite");
            intRetard.setCoutEstime(4500.00);
            intRetard.setDateDepot(LocalDateTime.now().minusDays(5));
            intRetard.setDateRestitutionPrevue(LocalDateTime.now().minusDays(1));
            intRetard.setMecanicien(mecElec);
            intRetard.setVehicule(vReel);
            Intervention savedIntRetard = interventionService.createIntervention(intRetard);

            HistoriqueIntervention histRetard = new HistoriqueIntervention();
            histRetard.setCommentaire("Passage en réparation après validation du devis par téléphone");
            histRetard.setDate(LocalDateTime.now().minusDays(4));
            histRetard.setAuteur("Youssef BENNANI");
            histRetard.setIntervention(savedIntRetard);
            histRetard.setAncienStatus(Status.DEVIS_A_VALIDER);
            histRetard.setNouveauStatus(Status.EN_REPARATION);
            historiqueService.createHistorique(histRetard);


            // --- CAS 2 : NOUVELLE INTERVENTION DU JOUR (Statut REÇUE) ---
            Intervention intNouvelle = new Intervention();
            intNouvelle.setStatus(Status.RECUE); // Statut initial d'arrivée
            intNouvelle.setPriorite(Priorite.BASSE);
            intNouvelle.setType(TypeIntervention.PNEUMATIQUES);
            intNouvelle.setDescription("Changement de 2 pneus avant + Parallélisme");
            intNouvelle.setDateDepot(LocalDateTime.now()); // Reçue aujourd'hui
            intNouvelle.setDateRestitutionPrevue(LocalDateTime.now().plusHours(4));
            intNouvelle.setVehicule(vFictif);
            // Pas de mécanicien ni de coût estimé encore (Conforme aux règles RG-AUTO-05/06)
            Intervention savedIntNouvelle = interventionService.createIntervention(intNouvelle);

            HistoriqueIntervention histNouvelle = new HistoriqueIntervention();
            histNouvelle.setCommentaire("Ouverture du dossier d'accueil à l'atelier");
            histNouvelle.setDate(LocalDateTime.now());
            histNouvelle.setAuteur("Youssef BENNANI");
            histNouvelle.setIntervention(savedIntNouvelle);
            histNouvelle.setAncienStatus(null);
            histNouvelle.setNouveauStatus(Status.RECUE);
            historiqueService.createHistorique(histNouvelle);



        };
    }
}
