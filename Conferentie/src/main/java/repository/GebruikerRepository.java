package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import domein.Gebruiker;

public interface GebruikerRepository extends JpaRepository<Gebruiker, Long> {

    Optional<Gebruiker> findByEmail(String email);
    boolean existsByEmail(String email); 

}