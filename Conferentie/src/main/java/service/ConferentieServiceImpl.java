package service;

import domein.Evenement;
import domein.Gebruiker;
import domein.Lokaal;
import domein.Spreker;
import exception.MaxFavouritesReachedException;
import lombok.RequiredArgsConstructor;
import repository.EvenementRepository;
import repository.GebruikerRepository;
import repository.LokaalRepository;
import repository.SprekerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
        if (evenementRepository.existsByNaamAndDatum(evenement.getNaam(), evenement.getDatum())) {
            throw new IllegalArgumentException("Er bestaat al een event met deze naam op deze dag.");
        }

        if (evenementRepository.existsOverlapEvent(
                evenement.getLokaal(),
                evenement.getDatum(),
                evenement.getBegintijdstip(),
                evenement.getEindtijdstip())) {
            throw new IllegalArgumentException("Er is al een event in dit lokaal op dit tijdstip.");
        }

        evenementRepository.save(evenement);
    }

    @Override
    public void updateEvenement(Long id, Evenement nieuwEvenement) {
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
        bestaand.setSprekers(nieuwEvenement.getSprekers());
    }

    @Override
    public void deleteEvenement(Long id) {
   	
    	gebruikerRepository.deleteFavorietenByEvenementId(id);
        gebruikerRepository.flush();

        evenementRepository.findById(id).ifPresent(evenement -> {
            evenement.getGebruikers().clear();
            evenementRepository.save(evenement);

            evenementRepository.delete(evenement);
        });
    }


    @Override
    public void createLokaal(Lokaal lokaal) {
        lokaalRepository.save(lokaal);
    }

    @Override
    public void createSpreker(Spreker spreker) {
        sprekerRepository.save(spreker);
    }
    
    @Override
    public List<Evenement> getFavorieten(Long gebruikerId) {
        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        return gebruiker.getFavorieteEvenementen()
            .stream()
            .sorted(Comparator.comparing(Evenement::getDatum)
                              .thenComparing(Evenement::getBegintijdstip)
                              .thenComparing(Evenement::getNaam)).toList();
    }

    
    public static final int MAX_FAVS = 5;  


	@Override
	public void addFavouriteEvent(Long eventId, Long gebruikerId) {
	    Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
	        .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));
	    Evenement evenement = evenementRepository.findById(eventId)
	        .orElseThrow(() -> new IllegalArgumentException("Evenement niet gevonden"));
	
	    if (gebruiker.getFavorieteEvenementen().size() >= MAX_FAVS &&
	        !gebruiker.getFavorieteEvenementen().contains(evenement)) {
	
	        throw new MaxFavouritesReachedException();   // ← eigen runtime-exception
	    }
	
	    gebruiker.voegEvenementFavoriet(evenement);
	    gebruikerRepository.saveAndFlush(gebruiker);
	}
	
    @Override
    public void deleteFavouriteEvent(Long eventId, Long gebruikerId) {
        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));
        Evenement evenement = evenementRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("Evenement niet gevonden"));

        gebruiker.verwijderEvenementFavoriet(evenement);
        gebruikerRepository.save(gebruiker);
        gebruikerRepository.flush(); 
    }

    @Override
    public void deleteAllFavouriteEvents(Long gebruikerId) {
        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        gebruiker.verwijderAlleFavorieten();
        gebruikerRepository.save(gebruiker);
        gebruikerRepository.flush();
    }
     
    @Override
    public void deleteSpreker(Long id) {
        Spreker spreker = sprekerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Spreker niet gevonden"));

        boolean isInGebruik = evenementRepository.existsBySprekersContaining(spreker);
        if (isInGebruik) {
            throw new IllegalStateException("Deze spreker is nog gekoppeld aan een of meerdere evenementen.");
        }

        sprekerRepository.delete(spreker);
    }

    @Override
    public void deleteLokaal(Long id) {
        Lokaal lokaal = lokaalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lokaal niet gevonden"));

        boolean isInGebruik = evenementRepository.existsByLokaal(lokaal);
        if (isInGebruik) {
            throw new IllegalStateException("Dit lokaal is nog gekoppeld aan een of meerdere evenementen.");
        }

        lokaalRepository.delete(lokaal);
    }

}
