package domein.gebruiker;

import java.time.LocalDate;
import java.util.Set;

import domein.evenement.IEvenement;
import util.Rol;

public interface IGebruiker {

	long getId(); 
	String getNaam(); 
	String getVoornaam(); 
	LocalDate getGeboortedatum(); 
	Rol getRol(); 
	String getEmail(); 
	Set<IEvenement> getFavorieteEvenementen(); 
}
