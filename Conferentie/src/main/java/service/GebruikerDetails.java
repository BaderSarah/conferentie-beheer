package service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import domein.Evenement;
import domein.Gebruiker;

public class GebruikerDetails implements UserDetails {

    private final Gebruiker gebruiker;

    public GebruikerDetails(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public Gebruiker getGebruiker() { 
        return gebruiker;
    }
    
    public Set<Evenement> getFavorieteEvenementen() {
        return gebruiker.getFavorieteEvenementen(); 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + gebruiker.getRol()));
    }

    @Override public String getPassword()    { return gebruiker.getWachtwoord(); }
    @Override public String getUsername()    { return gebruiker.getEmail();      }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
