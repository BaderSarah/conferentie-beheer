package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import domein.Evenement;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findAllByOrderByDatumAscBegintijdstipAsc();

}
