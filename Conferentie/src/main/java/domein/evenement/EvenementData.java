package domein.evenement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

//public class EvenementData {
//
//	private final EvenementBeheerder eb;
//	
//	public EvenementData(EvenementBeheerder eb) {
//		this.eb = eb; 
//	}
//	
//	void populeerData() {
//		eb.addLokaal(new Lokaal("A123", 23));
//		eb.addLokaal(new Lokaal("B123", 23));
//		
//		eb.addSpreker(new Spreker("Doe", "Jane", "Jane.Doe@mail.com"));
//		eb.addSpreker(new Spreker("Wouters", "Hilde", "Hilde.Wouters@mail.com"));
//
//		Set<Spreker> sprekers = new HashSet<>();
//		sprekers.add(eb.geefSpecifiekSpreker(1L));
//		sprekers.add(eb.geefSpecifiekSpreker(2L));
//		
//		Lokaal lokaal = eb.geefSpecifiekLokaal(1L);
//
//		eb.addEvenement(new Evenement(
//		    "Tech Trends 2025",
//		    "Een blik op technologie van morgen",
//		    1234,
//		    70,
//		    49.99,
//		    LocalDate.of(2025, 5, 2),
//		    LocalTime.of(10, 0),
//		    LocalTime.of(12, 30),
//		    lokaal,
//		    sprekers
//		));
//	}
//}
//
