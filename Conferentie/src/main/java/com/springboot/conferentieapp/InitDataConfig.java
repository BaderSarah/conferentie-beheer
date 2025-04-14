package com.springboot.conferentieapp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import domein.Evenement;
import domein.Lokaal;
import domein.Spreker;
import repository.EvenementRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;

@Component
public class InitDataConfig implements CommandLineRunner {

	@Autowired
	private EvenementRepository eventRepo;
	
	@Autowired
	private LokaalRepository lokaalRepo;
	
	@Autowired
	private SprekerRepository sprekerRepo;

	@Override
	public void run(String... args) {

		Lokaal a101 = new Lokaal("A101", 80);
        Lokaal b202 = new Lokaal("B202", 60);
        lokaalRepo.save(a101);
        lokaalRepo.save(b202);

        // Sprekers
        Spreker eva = new Spreker("Eva",  "Janssens", "eva.janssens@mail.com");
        Spreker tom = new Spreker("Tom",  "De Wilde", "tom.dewilde@mail.com");
        Spreker ines = new Spreker("Ines", "Peeters", "ines.peeters@mail.com");

        sprekerRepo.save(eva);
        sprekerRepo.save(tom);
        sprekerRepo.save(ines);

        // Evenementen
        Evenement event1 = new Evenement(
                "AI in de Praktijk",
                "Praktische toepassingen van AI",
                1234,
                1,
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

	}

}