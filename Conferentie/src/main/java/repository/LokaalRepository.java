package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domein.Lokaal;

public interface LokaalRepository extends JpaRepository<Lokaal, Long> {
	
	boolean existsByNaam(String naam);

}