package service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domein.Evenement;
import domein.Gebruiker;
import exceptions.MaxFavouritesReachedException;
import lombok.RequiredArgsConstructor;
import repository.EvenementRepository;
import repository.GebruikerRepository;


@Service
@RequiredArgsConstructor
@Transactional
public class EvenementServiceImpl implements EvenementenService {

    private final EvenementRepository evenementRepository; 
    private final GebruikerRepository gebruikerRepository; 
    
    public static final int MAX_FAVS = 5;  
    
    @Override
    public Evenement createEvenement(Evenement evenement) {
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

        return evenementRepository.save(evenement);
    }

    @Override
    public Evenement updateEvenement(Long id, Evenement nieuwEvenement) {
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

        return evenementRepository.save(bestaand); 
    }


	@Override
	public Evenement getEvenement(Long id) {
		return evenementRepository.findById(id).orElse(null);
	}

	@Override
	public List<Evenement> getAllEvenements() {
		return evenementRepository.findAll();
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
    public List<Evenement> getFavorieten(Long gebruikerId) {
        Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
            .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));

        return gebruiker.getFavorieteEvenementen()
            .stream()
            .sorted(Comparator.comparing(Evenement::getDatum)
                              .thenComparing(Evenement::getBegintijdstip)
                              .thenComparing(Evenement::getNaam)).toList();
    }

	@Override
	public void addFavouriteEvent(Long eventId, Long gebruikerId) {
	    Gebruiker gebruiker = gebruikerRepository.findById(gebruikerId)
	        .orElseThrow(() -> new IllegalArgumentException("Gebruiker niet gevonden"));
	    Evenement evenement = evenementRepository.findById(eventId)
	        .orElseThrow(() -> new IllegalArgumentException("Evenement niet gevonden"));
	
	    if (gebruiker.getFavorieteEvenementen().size() >= MAX_FAVS &&
	        !gebruiker.getFavorieteEvenementen().contains(evenement)) {
	
	        throw new MaxFavouritesReachedException();
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
    public List<Evenement> getEvenementenByDatum(LocalDate datum) {
        return evenementRepository.findByDatum(datum);
    }


}
