package domein;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import validator.ValidEmail;

@Entity
@Table(name = "spreker")
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "naam", "voornaam", "email"})
public class Spreker implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private Long id; 

	@Getter
	@NotBlank(message = "{spreker.err.name.notblank}")
	@Pattern(regexp = "^[A-Za-z].*", message = "{spreker.err.name.pattern}")
	private String naam;

	@Getter
	@NotBlank(message = "{spreker.err.firstname.notblank}")
	@Pattern(regexp = "^[A-Za-z].*", message = "{spreker.err.firstname.pattern}")
	private String voornaam;

	@Getter
	@NotBlank(message = "{spreker.err.email.notblank}") 
	@Email(message = "{spreker.err.email.pattern}") 
	@Column(unique = true)
	private String email;
	
	@ManyToMany(mappedBy = "sprekers")
    @JsonIgnore
	@Getter
	private Set<Evenement> evenementen = new HashSet<>();


	public Spreker(String naam, String voornaam, String email) {
		this.naam = naam;
		this.voornaam = voornaam;
		this.email = email;
	}
}
