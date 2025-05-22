package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import domein.Evenement;
import domein.Gebruiker;

public interface GebruikerRepository extends JpaRepository<Gebruiker, Long> {

    Optional<Gebruiker> findByEmail(String email);
    boolean existsByEmail(String email); 
    
    @Query("""
            select e
            from Gebruiker g
            join g.favorieteEvenementen e
            where g.id = :gebruikerId
            order by e.begintijdstip asc,
                     upper(e.naam) asc
            """)
     List<Evenement> findSortedFavouritesByGebruikerId(@Param("gebruikerId") long gebruikerId);


    @EntityGraph(attributePaths = "favorieteEvenementen")
    Optional<Gebruiker> findWithFavorietenById(Long id);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM gebruiker_favoriete_evenementen WHERE evenement_id = :id", nativeQuery = true)
    void deleteFavorietenByEvenementId(@Param("id") Long id);


}