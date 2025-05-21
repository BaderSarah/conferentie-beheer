package com.springboot.conferentieapp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import domein.Evenement;
import domein.Gebruiker;
import domein.Lokaal;
import domein.Spreker;
import jakarta.validation.ConstraintViolationException;
import repository.EvenementRepository;
import repository.GebruikerRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;
import util.Rol;

@Component
public class InitDataConfig implements CommandLineRunner {

	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired
	private GebruikerRepository gebruikerRepo;
	
	@Autowired
	private EvenementRepository eventRepo;
	
	@Autowired
	private LokaalRepository lokaalRepo;
	
	@Autowired
	private SprekerRepository sprekerRepo;

	@Override
	public void run(String... args) {

		Lokaal a101 = new Lokaal("A101", 20);
        Lokaal b202 = new Lokaal("B202", 30);
        lokaalRepo.save(a101);
        lokaalRepo.save(b202);

        // Sprekers
        Spreker eva = new Spreker("Janssens", "Eva",  "eva.janssens@mail.com");
        Spreker tom = new Spreker("De Wilde", "Tom", "tom.dewilde@mail.com");
        Spreker ines = new Spreker("Peeters", "Ines","ines.peeters@mail.com");

        sprekerRepo.save(eva);
        sprekerRepo.save(tom);
        sprekerRepo.save(ines);

        // Evenementen
        Evenement event1 = new Evenement(
        	    "AI in de Praktijk",
        	    "Praktische toepassingen van AI",
        	    7896,
        	    7896 % 97, // = 37
        	    29.99,
        	    LocalDate.of(2025, 5, 2),
        	    LocalTime.of(14, 0),
        	    LocalTime.of(16, 0),
        	    a101,
        	    Set.of(eva, tom)
        	);

        	Evenement event2 = new Evenement(
        	    "Cybersecurity Basics",
        	    "Intro tot cybersecurity",
        	    2345,
        	    2345 % 97, // = 16
        	    19.99,
        	    LocalDate.of(2025, 5, 3),
        	    LocalTime.of(9, 0),
        	    LocalTime.of(12, 0),
        	    b202,
        	    Set.of(ines)
        	);

        	Evenement event3 = new Evenement(
        	    "Data Science Workshop",
        	    "Hands-on workshop over data-analyse technieken",
        	    3456,
        	    3456 % 97, // = 63
        	    49.99,
        	    LocalDate.of(2025, 5, 10),
        	    LocalTime.of(10, 0),
        	    LocalTime.of(13, 0),
        	    a101,
        	    Set.of(eva)
        	);

        	Evenement event4 = new Evenement(
        	    "Advanced AI Models",
        	    "Diepgaande sessie over neurale netwerken",
        	    4567,
        	    4567 % 97, // = 9
        	    59.99,
        	    LocalDate.of(2025, 5, 15),
        	    LocalTime.of(14, 30),
        	    LocalTime.of(17, 0),
        	    b202,
        	    Set.of(tom)
        	);

        	Evenement event5 = new Evenement(
        	    "Cybersecurity Advanced",
        	    "Verdieping in cybersecurity technieken en tools",
        	    5678,
        	    5678 % 97, // = 55
        	    39.99,
        	    LocalDate.of(2025, 5, 20),
        	    LocalTime.of(9, 30),
        	    LocalTime.of(12, 30),
        	    a101,
        	    Set.of(ines, eva)
        	);

        	Evenement event6 = new Evenement(
        	    "Intro to Quantum Computing",
        	    "Basisprincipes en aankomende toepassingen",
        	    6789,
        	    6789 % 97, // = 1
        	    44.99,
        	    LocalDate.of(2025, 5, 22),
        	    LocalTime.of(10, 0),
        	    LocalTime.of(12, 30),
        	    b202,
        	    Set.of(tom, eva)
        	);

        	Evenement event7 = new Evenement(
        	    "Ethics in AI",
        	    "Interactieve sessie over ethische vraagstukken",
        	    7890,
        	    7890 % 97, // = 3
        	    24.99,
        	    LocalDate.of(2025, 5, 25),
        	    LocalTime.of(14, 0),
        	    LocalTime.of(16, 30),
        	    a101,
        	    Set.of(ines)
        	);

        	Evenement event8 = new Evenement(
        	    "DevOps Fundamentals",
        	    "Toolchain & best practices voor continue integratie",
        	    8901,
        	    8901 % 97, // = 94
        	    34.99,
        	    LocalDate.of(2025, 5, 27),
        	    LocalTime.of(9, 0),
        	    LocalTime.of(12, 0),
        	    b202,
        	    Set.of(tom)
        	);

        	Evenement event9 = new Evenement(
        	    "Cloud Security Deep-Dive",
        	    "Beveiligingsstrategieën voor multi-cloud omgevingen",
        	    9012,
        	    9012 % 97, // = 92
        	    54.99,
        	    LocalDate.of(2025, 5, 29),
        	    LocalTime.of(13, 30),
        	    LocalTime.of(17, 0),
        	    a101,
        	    Set.of(eva, ines)
        	); 

        eventRepo.saveAll(List.of(event1, event2, event3, event4, event5,
                                  event6, event7, event8, event9));

        // gebruikers
        String userPsw = encoder.encode("user123"); 

        var user =
            Gebruiker.builder()
                .naam("lastname")
                .voornaam("firstname")
                .email("user@mail.com")
                .rol(Rol.USER)
                .wachtwoord(userPsw)
                .bevestigWachtwoord(userPsw)
                .build();

        String adminPsw = encoder.encode("admin123");

        var admin =
            Gebruiker.builder()
                .naam("lastname")
                .voornaam("firstname")
                .email("admin@mail.com")
                .rol(Rol.ADMIN)
                .wachtwoord(adminPsw)
                .bevestigWachtwoord(adminPsw)
                .build();

        
        gebruikerRepo.saveAll(Arrays.asList(admin, user));

        user.voegEvenementFavoriet(event1);
        user.voegEvenementFavoriet(event2);


        gebruikerRepo.save(user);
	}

}