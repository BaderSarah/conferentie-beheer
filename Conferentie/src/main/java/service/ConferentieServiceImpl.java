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

import java.util.HashSet;
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
    }

    @Override
    public void deleteEvenement(Long id) {
        Optional<Evenement> opt = evenementRepository.findById(id);
        if (opt.isEmpty()) return;

        Evenement evenement = opt.get();

        for (Gebruiker g : new HashSet<>(evenement.getGebruikers())) {
            g.getFavorieteEvenementen().remove(evenement);
        }

        gebruikerRepository.saveAll(evenement.getGebruikers());

        evenement.getGebruikers().clear(); // optioneel

        evenementRepository.delete(evenement);
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
    public Set<Evenement> getFavorieten(Long gebruikerId) {
        Gebruiker g = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));
        g.getFavorieteEvenementen().size();  
        return g.getFavorieteEvenementen();
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
    
    
    // NIEUW
    
//    @Override
//    public void deleteSpreker(Long id) {
//        Optional<Spreker> opt = sprekerRepository.findById(id);
//        if (opt.isEmpty()) return;
//
//        Spreker spreker = opt.get();
//
//        for (Evenement ev : spreker.getEvenementen()) {
//            deleteEvenement(ev.getId());
//        }
//
//        spreker.getEvenementen().clear();
//        sprekerRepository.delete(spreker);
//    }
//
//    @Override
//    public void deleteLokaal(Long id) {
//        Optional<Lokaal> opt = lokaalRepository.findById(id);
//        if (opt.isEmpty()) return;
//
//        Lokaal lokaal = opt.get();
//
//        for (Evenement ev : lokaal.getEvenementen()) {
//            deleteEvenement(ev.getId());
//        }
//
//
//        lokaal.getEvenementen().clear();
//        lokaalRepository.delete(lokaal);
//    }


}
