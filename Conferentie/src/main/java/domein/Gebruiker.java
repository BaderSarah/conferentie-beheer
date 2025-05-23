package domein;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.Rol;
import validator.OnCreate;
import validator.ValidEmail;
import validator.ValidPasswords;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@Data
@Builder
@ValidPasswords
@AllArgsConstructor
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "email")
@Table(name = "gebruikers")
public class Gebruiker implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int MAX_FAVORIET = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{gebruiker.err.name.notblank}")
    @Pattern(regexp = "^[A-Za-z].*", message = "{gebruiker.err.name.pattern}")
    @Column(nullable = false)
    private String naam;

    @NotBlank(message = "{gebruiker.err.firstname.notblank}")
    @Pattern(regexp = "^[A-Za-z].*", message = "{gebruiker.err.firstname.pattern}")
    @Column(nullable = false)
    private String voornaam;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Rol rol;

    @NotNull(message = "{gebruiker.err.email.notblank}")
    @Email(message = "{gebruiker.err.email.pattern}")
    @Column(nullable = false, unique = true)
    @ValidEmail
    private String email;

//    @Size(min = 4, max = 20)
    @NotBlank(message = "{gebruiker.err.password.notblank}")
    @Column(nullable = false)
    private String wachtwoord;
    
    @Transient
    @NotBlank(message = "{gebruiker.err.password.confirm}")
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
