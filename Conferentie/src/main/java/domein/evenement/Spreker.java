package domein.evenement;

import java.io.Serializable;

import jakarta.validation.constraints.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Spreker implements Serializable, ISpreker {

	private static final long serialVersionUID = 1L;

	@Getter
	private long id;

	@Getter
	@NotBlank(message = "Naam is verplicht")
	@Pattern(regexp = "^[A-Za-z].*", message = "Naam moet beginnen met een letter")
	private String naam;

	@Getter
	@NotBlank(message = "Voornaam is verplicht")
	@Pattern(regexp = "^[A-Za-z].*", message = "Voornaam moet beginnen met een letter")
	private String voornaam;

	@Getter
	@NotNull(message = "Email is verplicht")
	@Email(message = "Ongeldig e-mailadres")
	private String email;

	public Spreker(String naam, String voornaam, String email) {
		this.naam = naam;
		this.voornaam = voornaam;
		this.email = email;
	}
}
