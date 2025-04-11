package domein.gebruiker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.Rol;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domein.evenement.Evenement;
import domein.evenement.IEvenement;

@NoArgsConstructor
public class Gebruiker implements Serializable, IGebruiker {

	private static final long serialVersionUID = 1L;

	@Getter
	private long id;

	@Getter
	@NotBlank(message = "Naam mag niet leeg zijn")
	@Pattern(regexp = "^[A-Za-z].*", message = "Naam moet beginnen met een letter")
	private String naam;

	@Getter
	@NotBlank(message = "Voornaam mag niet leeg zijn")
	@Pattern(regexp = "^[A-Za-z].*", message = "Voornaam moet beginnen met een letter")
	private String voornaam;

	@Getter
	// @NotNull(message = "Rol mag niet null zijn")
	private Rol rol;

	@Getter
	@NotNull(message = "Email is verplicht")
	@Email(message = "Ongeldig e-mailadres")
	private String email;

	@Setter(AccessLevel.PROTECTED)
	private Set<Evenement> favorieteEvenementen = new HashSet<>();

	private String wachtwoord;

	private static final int MAX_FAVORIET = 5;

	public Gebruiker(String naam, String voornaam, 
	                 Rol rol, String email, String wachtwoord) {
		this.naam = naam;
		this.voornaam = voornaam;
		this.rol = rol;
		this.email = email;
		setWachtwoord(wachtwoord);
	}

	public void setWachtwoord(String wachtwoord) {
		if (wachtwoord == null || wachtwoord.length() < 8) {
			throw new IllegalArgumentException("Wachtwoord moet minimaal 8 tekens lang zijn.");
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		this.wachtwoord = encoder.encode(wachtwoord);
	}

	public Set<IEvenement> getFavorieteEvenementen() {
		return Collections.unmodifiableSet(favorieteEvenementen);
	}

	// *-------------------methods----------------------*
	
	public void voegEvenementFavoriet(Evenement evenement) {
		boolean bestaatAl = favorieteEvenementen.stream()
		        .anyMatch(ev -> ev.getId() == evenement.getId());  

		if (evenement == null) {
	        throw new IllegalArgumentException("Evenement mag niet null zijn.");
	    }
		
		 if (favorieteEvenementen.size() >= MAX_FAVORIET) {
		        throw new IllegalStateException("Je mag maximaal " + MAX_FAVORIET + " favoriete evenementen hebben.");
		 }

		 if (!bestaatAl) {
		     favorieteEvenementen.add(evenement);
		 } else {
		     throw new IllegalArgumentException("Evenement staat al in je favorieten.");
		 }
	}
	
	public void verwijderEvenementFavoriet(Evenement evenement) {
		boolean bestaat = favorieteEvenementen.stream()
		        .anyMatch(ev -> ev.getId() == evenement.getId());
		
		if (evenement == null) {
	        throw new IllegalArgumentException("Evenement mag niet null zijn.");
	    }

		 if (!bestaat) {
		     throw new IllegalArgumentException("Evenement bestaat niet in je favorieten.");
		 } else {
		     favorieteEvenementen.remove(evenement);
		 }
	}
	
	public void verwijderAlleFavorieten() {
		this.favorieteEvenementen = new HashSet<Evenement>(); 
	}
	
}

