package domein;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import util.Rol;
import validator.ValidEmail;
import validator.ValidPasswords;
import validator.groups.OnRegistration;

@Entity
@Data
@Builder
@ValidPasswords(groups = OnRegistration.class)
@AllArgsConstructor
@NoArgsConstructor 
@EqualsAndHashCode(of = "email")
@Table(name = "gebruikers")
public class Gebruiker implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MAX_FAVORIET = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{gebruiker.err.name.notblank}", groups = {Default.class, OnRegistration.class})
    @Pattern(regexp = "^[A-Za-z].*", message = "{gebruiker.err.name.pattern}", groups = {Default.class, OnRegistration.class})
    @Column(nullable = false)
    private String naam;

    @NotBlank(message = "{gebruiker.err.firstname.notblank}", groups = {Default.class, OnRegistration.class})
    @Pattern(regexp = "^[A-Za-z].*", message = "{gebruiker.err.firstname.pattern}", groups = {Default.class, OnRegistration.class})
    @Column(nullable = false)
    private String voornaam;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Rol rol;

    @NotNull(message = "{gebruiker.err.email.notblank}", groups = {Default.class, OnRegistration.class})
    @Email(message = "{gebruiker.err.email.pattern}", groups = {Default.class, OnRegistration.class})
    @Column(nullable = false, unique = true)
    @ValidEmail(groups = {Default.class, OnRegistration.class})
    private String email;


    @Column(nullable = false)
    private String wachtwoord;
    
    @Transient
    private String bevestigWachtwoord;

    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "gebruiker_favoriete_evenementen",
        joinColumns = @JoinColumn(name = "gebruiker_id"),
        inverseJoinColumns = @JoinColumn(name = "evenement_id")
    )
    @Getter private Set<Evenement> favorieteEvenementen = new HashSet<>();

    public void voegEvenementFavoriet(Evenement evenement) {
        if (favorieteEvenementen == null) {
            favorieteEvenementen = new HashSet<>();
        }
        if (evenement == null) {
            throw new IllegalArgumentException("Evenement mag niet null zijn.");
        }

        boolean bestaatAl = favorieteEvenementen.stream()
                .anyMatch(ev -> Objects.equals(ev.getId(), evenement.getId()));

        if (favorieteEvenementen.size() >= MAX_FAVORIET) {
            throw new IllegalStateException("Je mag maximaal " + MAX_FAVORIET + " favoriete evenementen hebben.");
        }

        if (bestaatAl) {
            throw new IllegalArgumentException("Evenement staat al in je favorieten.");
        }

        favorieteEvenementen.add(evenement);
    }

    public void verwijderEvenementFavoriet(Evenement evenement) {

    	this.favorieteEvenementen.remove(evenement);
        evenement.getGebruikers().remove(this);
    }

    public void verwijderAlleFavorieten() {
        this.favorieteEvenementen.clear();
    }
}
