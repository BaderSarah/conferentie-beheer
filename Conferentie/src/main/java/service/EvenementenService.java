package service;

import java.time.LocalDate;
import java.util.List;

import domein.Evenement;

public interface EvenementenService {

    Evenement createEvenement(Evenement evenement);

    Evenement updateEvenement(Long id, Evenement nieuwEvenement);
    
    void deleteEvenement(Long id);
    
    Evenement getEvenement(Long id); 
    
    List<Evenement> getAllEvenements(); 

    List<Evenement> getFavorieten(Long id);
    
    void addFavouriteEvent(Long eventId, Long gebruikerId);

    void deleteFavouriteEvent(Long eventId, Long gebruikerId);

    void deleteAllFavouriteEvents(Long gebruikerId);
    
    List<Evenement> getEvenementenByDatum(LocalDate datum);

}
