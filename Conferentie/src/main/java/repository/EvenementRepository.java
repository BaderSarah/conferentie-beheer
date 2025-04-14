package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import domein.Evenement;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

}
