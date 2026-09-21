package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.enums.*;
import com.example.demo.services.*;
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
    CommandLineRunner start(IUserService userService,
                            IInterventionService interventionService,
                            IMechanicService mechanicService,
                            IVehicleService vehicleService,
                            IInterventionHistoryService interventionHistoryService) {
        return args -> {

            User user1 = new User();
            user1.addRole(Role.ADMIN);
            user1.addRole(Role.CONSEILLER);
            user1.setFirstName("Abdelhafid");
            user1.setLastName("EL AMAL");
            user1.setPassword("k10888");
            user1.setEnabled(true);
            user1.setEmail("abdelhafid.el-amal@capgemini.com");
            userService.createUser(user1);

            User user2 = new User();
            user2.addRole(Role.TECHNICIEN);
            user2.setFirstName("Chakib");
            user2.setLastName("EL IROUI");
            user2.setPassword("k100000");
            user2.setEnabled(false);
            user2.setEmail("chakib.el-iroui@capgemini.com");
            userService.createUser(user2);

            Mechanic mechanic = new Mechanic();
            mechanic.setSpecialty(Specialty.DIAGNOSTICS);
            mechanic.setName("mec001");
            mechanic.setAvailable(true);
            mechanicService.createMechanic(mechanic);

            Vehicle vehicle = new Vehicle();
            vehicle.setMatricule("Maroc-01-A");
            vehicle.setMileage(2000);
            vehicle.setModel("M-2023");
            vehicle.setYear(2023);
            vehicle.setMake("BMW");
            vehicle.setDummyClient(true);
            vehicleService.createVehicle(vehicle);

            Intervention intervention = new Intervention();
            intervention.setStatus(Status.RECEIVED);
            intervention.setDiagnostic("diagnostic Numero 001");
            intervention.setPriority(Priority.MEDIUM);
            intervention.setType(InterventionType.DIAGNOSTIC);
            intervention.setClosureDate(LocalDateTime.now());
            intervention.setEstimatedCost(1500.00);
            intervention.setDepositDate(LocalDateTime.now());
            intervention.setMechanic(mechanic);
            intervention.setVehicle(vehicle);
            intervention.setDescription("vehicle bmw m4 cs");
            intervention.setEstimatedReturnDate(LocalDateTime.MAX);

            Intervention savedIntervention = interventionService.createIntervention(intervention);

            InterventionHistory hist = new InterventionHistory();
            hist.setComment("Diagnostic at : " + LocalDateTime.now());
            hist.setDate(LocalDateTime.now());
            hist.setAuthor("Mohsin EL AMAL");
            hist.setIntervention(savedIntervention);
            hist.setOldStatus(savedIntervention.getStatus());
            hist.setNewStatus(Status.DIAGNOSTIC_IN_PROGRESS);
            interventionHistoryService.createHistory(hist);

            User advisor = new User();
            advisor.addRole(Role.CONSEILLER);
            advisor.setLastName("BENNANI");
            advisor.setFirstName("Youssef");
            advisor.setPassword("at1020");
            advisor.setEnabled(true);
            advisor.setEmail("youssef.bennani@capgemini.com");
            userService.createUser(advisor);

            User manager = new User();
            manager.addRole(Role.MANAGER);
            manager.setLastName("ALAMI");
            manager.setFirstName("Amine");
            manager.setPassword("mgr3000");
            manager.setEnabled(true);
            manager.setEmail("amine.alami@capgemini.com");
            userService.createUser(manager);

            Mechanic mecElec = new Mechanic();
            mecElec.setSpecialty(Specialty.ELECTRICAL_ELECTRONICS);
            mecElec.setName("Hassan Bouras");
            mecElec.setAvailable(true);
            mechanicService.createMechanic(mecElec);

            Mechanic mecPneu = new Mechanic();
            mecPneu.setSpecialty(Specialty.TIRES);
            mecPneu.setName("Karim Tazi");
            mecPneu.setAvailable(false);
            mechanicService.createMechanic(mecPneu);

            Vehicle vReal = new Vehicle();
            vReal.setMatricule("Maroc-99-B-1234");
            vReal.setMileage(85000);
            vReal.setModel("Golf 7");
            vReal.setYear(2019);
            vReal.setMake("Volkswagen");
            vReal.setDummyClient(false);
            vehicleService.createVehicle(vReal);

            Vehicle vDummy = new Vehicle();
            vDummy.setMatricule("Maroc-77-X-5678"); // Variable corrigée ici (vFictif -> vDummy)
            vDummy.setMileage(12000);
            vDummy.setModel("Clio 5");
            vDummy.setYear(2021);
            vDummy.setMake("Renault");
            vDummy.setDummyClient(true);
            vehicleService.createVehicle(vDummy);

            Intervention intDelayed = new Intervention();
            intDelayed.setStatus(Status.UNDER_REPAIR);
            intDelayed.setPriority(Priority.HIGH);
            intDelayed.setType(InterventionType.REPAIR);
            intDelayed.setDescription("Changement d'embrayage complet");
            intDelayed.setDiagnostic("Butée d'embrayage totalement détruite");
            intDelayed.setEstimatedCost(4500.00);
            intDelayed.setDepositDate(LocalDateTime.now().minusDays(5));
            intDelayed.setEstimatedReturnDate(LocalDateTime.now().minusDays(1));
            intDelayed.setMechanic(mecElec);
            intDelayed.setVehicle(vReal);
            Intervention savedIntDelayed = interventionService.createIntervention(intDelayed);

            InterventionHistory histDelayed = new InterventionHistory();
            histDelayed.setComment("Passage en réparation après validation du devis par téléphone");
            histDelayed.setDate(LocalDateTime.now().minusDays(4));
            histDelayed.setAuthor("Youssef BENNANI");
            histDelayed.setIntervention(savedIntDelayed);
            histDelayed.setOldStatus(Status.QUOTATION_TO_VALIDATE);
            histDelayed.setNewStatus(Status.UNDER_REPAIR);
            interventionHistoryService.createHistory(histDelayed);

            Intervention intNew = new Intervention();
            intNew.setStatus(Status.RECEIVED);
            intNew.setPriority(Priority.LOW);
            intNew.setType(InterventionType.REPAIR);
            intNew.setEstimatedCost(4500.00);
            intNew.setDescription("Changement de 2 pneus avant + Parallélisme");
            intNew.setDepositDate(LocalDateTime.now());
            intNew.setEstimatedReturnDate(LocalDateTime.now().plusHours(4));
            intNew.setVehicle(vDummy);
            Intervention savedIntNew = interventionService.createIntervention(intNew);

            InterventionHistory histNew = new InterventionHistory();
            histNew.setComment("Ouverture du dossier d'accueil à l'atelier");
            histNew.setDate(LocalDateTime.now());
            histNew.setAuthor("Youssef BENNANI");
            histNew.setIntervention(savedIntNew);
            histNew.setOldStatus(null);
            histNew.setNewStatus(Status.RECEIVED);
            interventionHistoryService.createHistory(histNew);
        };
    }
}
