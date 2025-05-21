package service;

import java.util.Set;

import domein.Evenement;
import domein.Lokaal;
import domein.Spreker;


public interface ConferentieService {

    void createEvenement(Evenement evenement);

    void updateEvenement(Long id, Evenement nieuwEvenement);
    
//    void deleteSpreker(Long id); 
//    
//    void deleteLokaal(Long id); 

    void deleteEvenement(Long id);

    void createLokaal(Lokaal lokaal);

    void createSpreker(Spreker spreker);

    Set<Evenement> getFavorieten(Long id);
    
    void addFavouriteEvent(Long eventId, Long gebruikerId);

    void deleteFavouriteEvent(Long eventId, Long gebruikerId);

    void deleteAllFavouriteEvents(Long gebruikerId);
    
}
