package service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domein.Lokaal;
import lombok.RequiredArgsConstructor;
import repository.EvenementRepository;
import repository.LokaalRepository;


@Service
@RequiredArgsConstructor
@Transactional
public class LokaalServiceImpl implements LokaalService {

    private final LokaalRepository lokaalRepository; 
    private final EvenementRepository evenementRepository; 
	
    @Override
    public Lokaal createLokaal(Lokaal lokaal) {
        return lokaalRepository.save(lokaal);
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

	@Override
	public Lokaal getLokaal(Long id) {
		return lokaalRepository.findById(id).orElse(null); 
	}

	@Override
	public List<Lokaal> getAllLokaals() {
		return lokaalRepository.findAll();
	}
	
	@Override
	public int getCapaciteitVanLokaal(Long id) {
	    return lokaalRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("Lokaal niet gevonden"))
	        .getCapaciteit();
	}


}
