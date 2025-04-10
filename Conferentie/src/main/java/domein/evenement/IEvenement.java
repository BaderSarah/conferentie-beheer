package domein.evenement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public interface IEvenement {
	
	long getId(); 
	String getNaam(); 
	String getBeschrijving(); 
	int getBeamercode(); 
	int getBeamercheck(); 
	double getPrijs(); 
	LocalDate getDatum(); 
	LocalTime getBegintijdstip(); 
	LocalTime getEindtijdstip(); 
	ILokaal getLokaal(); 
	Set<Spreker> getSprekers(); 

	}
