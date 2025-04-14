package domein;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Range;

@Entity
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor()
public class Evenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter private long id; 

    @Getter
    @NotBlank(message = "{event.err.name.notblank}")
    @Pattern(regexp = "^[A-Za-z].*", message = "{event.err.name.pattern}")
    private String naam;

    @Getter
    private String beschrijving;

    @Getter
    @Range(min = 1000, max = 9999, message = "{event.err.projectorCode}")
    private int beamercode;

    @Getter
    private int beamercheck; 

    @Getter
    @DecimalMin(value = "9.99", inclusive = true, message = "{event.err.price.min}")
    @DecimalMax(value = "99.99", inclusive = true, message = "{event.err.price.max}")
    private double prijs;

    @Getter
    @NotNull(message = "{event.err.date}")
    private LocalDate datum;

    @Getter
    @NotNull(message = "{event.err.starttime}")
    private LocalTime begintijdstip;

    @Getter
    @NotNull(message = "{event.err.endtime}")
    private LocalTime eindtijdstip;

    @Getter
    @NotNull(message = "{event.err.room}")
    @ManyToOne
    private Lokaal lokaal;

    @Size(min = 1, max = 3, message = "{event.err.speakers}")
    @ManyToMany
    private Set<Spreker> sprekers = new HashSet<>();

    @Transient private static final LocalDate CONFERENTIE_START = LocalDate.of(2025, 5, 1);
    @Transient private static final LocalDate CONFERENTIE_EIND = LocalDate.of(2025, 5, 5);
    @Transient private static final int MAX_SPREKERS = 3;

    public Evenement(String naam, String beschrijving, int beamercode,
                     int beamercheck, double prijs, LocalDate datum,
                     LocalTime beginTijdstip, LocalTime eindTijdstip,
                     Lokaal lokaal, Set<Spreker> sprekers) {

        setNaam(naam);
        this.beschrijving = beschrijving;
        setBeamercode(beamercode);
        setBeamercheck(beamercheck);
        setPrijs(prijs);
        setDatum(datum);
        setBegintijdstip(beginTijdstip);
        setEindTijdstip(eindTijdstip);
        setLokaal(lokaal);
        setSprekers(sprekers);
    }

    // ------- Setters met extra logica later naar Validator -------

    private void setDatum(LocalDate datum) {
        if (datum.isBefore(CONFERENTIE_START) || datum.isAfter(CONFERENTIE_EIND)) {
            throw new IllegalArgumentException("Datum moet binnen de conferentieperiode vallen.");
        }
        this.datum = datum;
    }

    private void setBeamercheck(int beamercheck) {
//        if (this.beamercode % 97 != beamercheck) {
//            throw new IllegalArgumentException("Beamercheck is niet correct (beamercode % 97).");
//        }
        this.beamercheck = beamercheck;
    }

    private void setSprekers(Set<Spreker> sprekers) {
        if (sprekers == null || sprekers.isEmpty()) {
            throw new IllegalArgumentException("Er moeten minstens één spreker zijn.");
        }
        if (sprekers.size() > 3) {
            throw new IllegalArgumentException("Maximum 3 sprekers toegestaan.");
        }
        Set<String> emails = new HashSet<>();
        for (Spreker s : sprekers) {
            if (!emails.add(s.getEmail())) {
                throw new IllegalArgumentException("Dubbele spreker gevonden (zelfde e-mailadres).");
            }
        }
        this.sprekers = sprekers;
    }

    private void setEindTijdstip(LocalTime eindTijdstip) {
        if (eindTijdstip == null) {
            throw new IllegalArgumentException("Eindtijdstip mag niet null zijn.");
        }
        if (this.begintijdstip != null && eindTijdstip.isBefore(this.begintijdstip)) {
            throw new IllegalArgumentException("Eindtijdstip moet na begintijdstip liggen.");
        }
        this.eindtijdstip = eindTijdstip;
    }

    // ------- Getters en Methoden -------

    public Set<Spreker> getSprekers() {
        return Collections.unmodifiableSet(sprekers);
    }

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
}
