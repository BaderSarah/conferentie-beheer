package repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import domein.Evenement;
import domein.Lokaal;
import domein.Spreker;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findAllByOrderByDatumAscBegintijdstipAsc();

    boolean existsByNaamAndDatum(String naam, LocalDate datum);

    @Query("""
        select case when count(e) > 0 then true else false end
        from Evenement e 
        where e.lokaal = :lokaal
          and e.datum = :datum
          and (
              (:begin < e.eindtijdstip and :eind > e.begintijdstip)
          )
    """)
    boolean existsOverlapEvent(@Param("lokaal") Lokaal lokaal,
                               @Param("datum") LocalDate datum,
                               @Param("begin") LocalTime begintijdstip,
                               @Param("eind") LocalTime eindtijdstip);

    boolean existsBySprekersContaining(Spreker spreker);
    boolean existsByLokaal(Lokaal lokaal);
    List<Evenement> findByDatum(LocalDate datum);

}
