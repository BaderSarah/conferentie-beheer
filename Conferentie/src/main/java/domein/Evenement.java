package domein;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import validator.ValidBeamerCheck;
import validator.ValidConferenceDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "evenement")
@Data
@ValidConferenceDate
@ValidBeamerCheck
@NoArgsConstructor()
@EqualsAndHashCode(of = "id") 
public class Evenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 

    @NotBlank(message = "{event.err.name.notblank}")
    @Pattern(regexp = "^[A-Za-z].*", message = "{event.err.name.pattern}")
    private String naam;

    private String beschrijving;

    @Range(min = 1000, max = 9999, message = "{event.err.projectorCode}")
    private int beamercode;

    private int beamercheck; 

    @DecimalMin(value = "9.99", inclusive = true, message = "{event.err.price.min}")
    @DecimalMax(value = "99.99", inclusive = true, message = "{event.err.price.max}")
//    @Digits(integer = 2, fraction = 2, message = "Prijs moet 2 decimalen hebben") // #TODO
    private double prijs;


    // @DateInConferenceRange(start = "2025-05-01", end = "2025-05-05", message = "{event.err.date.range}")
    @NotNull(message = "{event.err.date}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate datum;

    @NotNull(message = "{event.err.starttime}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime begintijdstip;


    @NotNull(message = "{event.err.endtime}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime eindtijdstip;

    @NotNull(message = "{event.err.room}")
    @ManyToOne
    private Lokaal lokaal;

    @Size(min = 1, max = 3, message = "{event.err.speakers}")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "evenement_sprekers",
            joinColumns = @JoinColumn(name = "evenement_id"),
            inverseJoinColumns = @JoinColumn(name = "spreker_id")
        )
    private List<Spreker> sprekers = new ArrayList<>();

    @Transient private static final int MAX_SPREKERS = 3;
    
    @ManyToMany(mappedBy = "favorieteEvenementen")
    @Getter private Set<Gebruiker> gebruikers = new HashSet<>();

    public Evenement(String naam, String beschrijving, int beamercode,
                     int beamercheck, double prijs, LocalDate datum,
                     LocalTime beginTijdstip, LocalTime eindTijdstip,
                     Lokaal lokaal, List<Spreker> sprekers) {

        setNaam(naam);
        this.beschrijving = beschrijving;
        setBeamercode(beamercode);
        setBeamercheck(beamercheck);
        setPrijs(prijs);
        setDatum(datum);
        setBegintijdstip(beginTijdstip);
        setEindtijdstip(eindTijdstip);
        setLokaal(lokaal);
        setSprekers(sprekers);
    }

    // ------- Setter en Methoden -------

    public void voegSprekerToe(Spreker spreker) {
        if (spreker == null) {
            throw new IllegalArgumentException("Spreker mag niet null zijn.");
        }
        boolean bestaatAl = sprekers.stream().anyMatch(ev -> ev.getId() == spreker.getId());
        if (sprekers.size() >= MAX_SPREKERS) {
            throw new IllegalStateException("Je mag maximaal " + MAX_SPREKERS + " sprekers hebben.");
        }
        if (!bestaatAl) {
            sprekers.add(spreker);
        } else {
            throw new IllegalArgumentException("Evenement staat al in je favorieten.");
        }
    }

    public void verwijderSpreker(Spreker spreker) {
        if (spreker == null) {
            throw new IllegalArgumentException("Spreker mag niet null zijn.");
        }
        if (sprekers.size() == 1) {
            throw new IllegalArgumentException("Er moet minimaal 1 spreker geselecteerd zijn.");
        }
        boolean bestaat = sprekers.stream().anyMatch(ev -> ev.getId() == spreker.getId());
        if (!bestaat) {
            throw new IllegalArgumentException("Spreker bestaat niet in je geselecteerde sprekers.");
        } else {
            sprekers.remove(spreker);
        }
    }
    
    public void verwijderGebruikerFavoriet(Gebruiker gebruiker) {
        if (gebruiker != null) {
            gebruikers.remove(gebruiker);
            gebruiker.getFavorieteEvenementen().remove(this);
        }
    }


}
