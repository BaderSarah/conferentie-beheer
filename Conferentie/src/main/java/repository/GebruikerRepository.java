package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domein.Gebruiker;

public interface GebruikerRepository extends JpaRepository<Gebruiker, Long> {

}