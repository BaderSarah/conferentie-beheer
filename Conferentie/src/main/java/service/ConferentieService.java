package service;

import java.util.Set;

import domein.Evenement;
import domein.Lokaal;
import domein.Spreker;


public interface ConferentieService {

    void createEvenement(Evenement evenement);

    void updateEvenement(long id, Evenement nieuwEvenement);

    void deleteEvenement(long id);

    void createLokaal(Lokaal lokaal);

    void deleteLokaal(long id);

    void createSpreker(Spreker spreker);

    void deleteSpreker(long id);

    Set<Evenement> getFavorieten(long id);
    
    void addFavouriteEvent(long eventId, long gebruikerId);

    void deleteFavouriteEvent(long eventId, long gebruikerId);

    void deleteAllFavouriteEvents(long gebruikerId);
    
}
