package domein.evenement;

import java.io.Serializable;

import jakarta.validation.constraints.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lokaal implements Serializable, ILokaal {

	private static final long serialVersionUID = 1L;

	@Getter
	private long id;

	@Getter
	@NotNull(message = "Naam mag niet null zijn")
	@Pattern(regexp = "^[A-Za-z]\\d{3}$", message = "Naam moet beginnen met een letter gevolgd door exact 3 cijfers (bijv. A123)")
	private String naam;

	@Getter
	@Min(value = 1, message = "Capaciteit moet minstens 1 zijn")
	@Max(value = 50, message = "Capaciteit mag maximaal 50 zijn")
	private int capaciteit;

	public Lokaal(String naam, int capaciteit) {
		this.naam = naam;
		this.capaciteit = capaciteit;
	}
}
