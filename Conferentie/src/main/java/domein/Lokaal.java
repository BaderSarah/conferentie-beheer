package domein;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lokaal")
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "naam", "capaciteit"})
public class Lokaal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private Long id; 

	@Getter
	@NotNull(message = "{lokaal.err.name.notblank}")
	@Pattern(regexp = "^[A-Za-z]\\d{3}$", message = "{lokaal.err.name.pattern}")
	@Column(unique=true)
	private String naam;

	@Getter
    @Range(min = 1, max = 50, message = "{lokaal.err.capacity}")
	private int capaciteit;
	
	@OneToMany(mappedBy = "lokaal", cascade = CascadeType.ALL, orphanRemoval = false)
	@Getter
	@JsonIgnore
	private Set<Evenement> evenementen = new HashSet<>();

	public Lokaal(String naam, int capaciteit) {
		this.naam = naam;
		this.capaciteit = capaciteit;
	}
}
