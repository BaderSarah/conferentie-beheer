package domein;

import java.io.Serializable;

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
@Table(name = "spreker")
@Setter
@NoArgsConstructor
public class Spreker implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private long id; 

	@Getter
	@NotBlank(message = "{spreker.err.name.notblank}")
	@Pattern(regexp = "^[A-Za-z].*", message = "{spreker.err.name.pattern}")
	private String naam;

	@Getter
	@NotBlank(message = "{spreker.err.firstname.notblank}")
	@Pattern(regexp = "^[A-Za-z].*", message = "{spreker.err.firstname.pattern}")
	private String voornaam;

	@Getter
	@NotNull(message = "{spreker.err.email.notblank}")
	@Email(message = "{spreker.err.email.pattern}")
	@Column(unique=true)
	private String email;

	public Spreker(String naam, String voornaam, String email) {
		this.naam = naam;
		this.voornaam = voornaam;
		this.email = email;
	}
}
