package domein.gebruiker;

import java.util.Set;

import domein.evenement.IEvenement;
import util.Rol;

public interface IGebruiker {

	long getId(); 
	String getNaam(); 
	String getVoornaam(); 
	Rol getRol(); 
	String getEmail(); 
	Set<IEvenement> getFavorieteEvenementen(); 
}
