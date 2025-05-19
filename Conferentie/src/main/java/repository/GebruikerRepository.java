package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


//     @Query("""
//    		  select g from Gebruiker g
//    		  left join fetch g.favorieteEvenementen fe
//    		  where g.id = :id
//    		  order by fe.datum asc, fe.begintijdstip asc
//    		""")
//    Optional<Gebruiker> findWithFavouritesSorted(long id);
}