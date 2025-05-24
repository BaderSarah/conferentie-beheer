package service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domein.Lokaal;
import domein.Spreker;
import lombok.RequiredArgsConstructor;
import repository.EvenementRepository;
import repository.SprekerRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class SprekerServiceImpl implements SprekerService {

    private final SprekerRepository sprekerRepository;
    private final EvenementRepository evenementRepository; 
	
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
	public Spreker createSpreker(Spreker spreker) {
        return sprekerRepository.save(spreker);		
	}

	@Override
	public Spreker getSpreker(Long id) {
		return sprekerRepository.findById(id).orElse(null);
	}

	@Override
	public List<Spreker> getAllSprekers() {
		return sprekerRepository.findAll();
	}

	@Override
	public Spreker updateSpreker(Long id, Spreker sprekerDetails) {
		Spreker spreker = sprekerRepository.findById(id)
			        .orElseThrow(() -> new IllegalArgumentException("Spreker niet gevonden met id: " + id));

		spreker.setVoornaam(sprekerDetails.getVoornaam());
		spreker.setNaam(sprekerDetails.getNaam());
		spreker.setEmail(sprekerDetails.getEmail());

	   return sprekerRepository.save(spreker);
	}

}
