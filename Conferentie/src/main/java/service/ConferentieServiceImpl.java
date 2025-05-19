package service;

import domein.Evenement;
import domein.Gebruiker;
import domein.Lokaal;
import domein.Spreker;
import lombok.RequiredArgsConstructor;
import repository.EvenementRepository;
import repository.GebruikerRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ConferentieServiceImpl implements ConferentieService {

    private final EvenementRepository evenementRepository; 
    private final SprekerRepository sprekerRepository; 
    private final LokaalRepository lokaalRepository; 
    private final GebruikerRepository gebruikerRepository; 

    @Override
    public void createEvenement(Evenement evenement) {
        evenementRepository.save(evenement);
    }

    @Override
    public void updateEvenement(long id, Evenement nieuwEvenement) {
        Evenement bestaand = evenementRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Evenement niet gevonden met id: " + id));
        
        bestaand.setNaam(nieuwEvenement.getNaam());
        bestaand.setBeschrijving(nieuwEvenement.getBeschrijving());
        bestaand.setBeamercode(nieuwEvenement.getBeamercode());
        bestaand.setBeamercheck(nieuwEvenement.getBeamercheck());
        bestaand.setPrijs(nieuwEvenement.getPrijs());
        bestaand.setDatum(nieuwEvenement.getDatum());
        bestaand.setBegintijdstip(nieuwEvenement.getBegintijdstip());
        bestaand.setEindtijdstip(nieuwEvenement.getEindtijdstip());
        bestaand.setLokaal(nieuwEvenement.getLokaal());
        // evt. sprekers ook updaten als gewenst
    }

    @Override
    public void deleteEvenement(long id) {
        evenementRepository.deleteById(id);
    }

    @Override
    public void createLokaal(Lokaal lokaal) {
        lokaalRepository.save(lokaal);
    }

    @Override
    public void deleteLokaal(long id) {
        lokaalRepository.deleteById(id);
    }

    @Override
    public void createSpreker(Spreker spreker) {
        sprekerRepository.save(spreker);
    }

    @Override
    public void deleteSpreker(long id) {
        sprekerRepository.deleteById(id);
    }
    
    @Override
    public Set<Evenement> getFavorieten(long gebruikerId) {
        Gebruiker g = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));
        g.getFavorieteEvenementen().size();  // om lazy loading te forceren
        return g.getFavorieteEvenementen();
    }

    @Override
    public void addFavouriteEvent(long eventId, long gebruikerId) {
        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));
        Evenement evenement = evenementRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("Evenement niet gevonden"));

        gebruiker.voegEvenementFavoriet(evenement);
        gebruikerRepository.save(gebruiker);
        gebruikerRepository.flush();
    }

    @Override
    public void deleteFavouriteEvent(long eventId, long gebruikerId) {
        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));
        Evenement evenement = evenementRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("Evenement niet gevonden"));

        gebruiker.verwijderEvenementFavoriet(evenement);
        gebruikerRepository.save(gebruiker);
        gebruikerRepository.flush(); 
    }

    @Override
    public void deleteAllFavouriteEvents(long gebruikerId) {
        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        gebruiker.verwijderAlleFavorieten();
        gebruikerRepository.save(gebruiker);
        gebruikerRepository.flush();
    }
}
