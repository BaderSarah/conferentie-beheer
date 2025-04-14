package domein.evenement;

import java.io.Serializable;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Lokaal implements Serializable, ILokaal {

	private static final long serialVersionUID = 1L;

	@Getter
	private long id;

	@Getter
	@NotNull(message = "{lokaal.err.name.notblank}")
	@Pattern(regexp = "^[A-Za-z]\\d{3}$", message = "{lokaal.err.name.pattern}")
	private String naam;

	@Getter
    @Range(min = 1, max = 50, message = "{lokaal.err.capacity}")
	private int capaciteit;

	public Lokaal(String naam, int capaciteit) {
		this.naam = naam;
		this.capaciteit = capaciteit;
	}
}
