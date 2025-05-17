package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domein.Spreker;

public interface SprekerRepository extends JpaRepository<Spreker, Long> {
	
    boolean existsByEmail(String email);

}