package domein;

import java.io.Serializable;

import org.hibernate.validator.constraints.Range;

import domein.evenement.ILokaal;
import jakarta.persistence.Column;
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

@Entity
@Table(name = "lokaal")
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Lokaal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private long id; 

	@Getter
	@NotNull(message = "{lokaal.err.name.notblank}")
	@Pattern(regexp = "^[A-Za-z]\\d{3}$", message = "{lokaal.err.name.pattern}")
	@Column(unique=true)
	private String naam;

	@Getter
    @Range(min = 1, max = 50, message = "{lokaal.err.capacity}")
	private int capaciteit;

	public Lokaal(String naam, int capaciteit) {
		this.naam = naam;
		this.capaciteit = capaciteit;
	}
}
