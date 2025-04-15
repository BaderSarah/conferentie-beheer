package domein;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.Rol;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import domein.evenement.IEvenement;
import domein.gebruiker.IGebruiker;

@Entity
@Table(name = "gebruiker")
@NoArgsConstructor
public class Gebruiker implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; 

	@Getter
	@NotBlank(message = "{gebruiker.err.name.notblank}")
	@Pattern(regexp = "^[A-Za-z].*", message = "{gebruiker.err.name.pattern}")
	private String naam;

	@Getter
	@NotBlank(message = "{gebruiker.err.firstname.notblank}")
	@Pattern(regexp = "^[A-Za-z].*", message = "{gebruiker.err.firstname.pattern}")
	private String voornaam;

	@Getter
	// @NotNull(message = "Rol mag niet null zijn")
	private Rol rol;

	@Getter
	@NotNull(message = "{gebruiker.err.email.notblank}")
	@Email(message = "{gebruiker.err.email.pattern}")
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

	public Set<Evenement> getFavorieteEvenementen() {
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

