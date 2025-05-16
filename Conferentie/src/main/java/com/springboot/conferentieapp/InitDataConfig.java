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
                39,
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
                0,
                19.99,
                LocalDate.of(2025, 5, 3),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                b202,
                Set.of(ines)
        );

        eventRepo.save(event1);
        eventRepo.save(event2);

        // users
        
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
	        
		List<Gebruiker> userList =  Arrays.asList(admin, user);
		gebruikerRepo.saveAll(userList);
	}

}