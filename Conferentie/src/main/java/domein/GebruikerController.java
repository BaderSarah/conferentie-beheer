package domein;

//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//import domein.evenement.EvenementBeheerder;
//import domein.evenement.IEvenement;
//import domein.gebruiker.Gebruiker;
//import domein.gebruiker.GebruikerBeheerder;
//import domein.gebruiker.IGebruiker;
//import util.Rol;

//public class GebruikerController {
//	
//	private GebruikerBeheerder gb = new GebruikerBeheerder(); 
//	private EvenementBeheerder eb = new EvenementBeheerder(); 
//	
//    public void close() {
//        gb.closePersistentie();
//    }
//    
//    // login + registratie (add user?)
//    
//	// *-------------------adding----------------------*
//
//	public void addGebruiker(String naam, String voornaam, 
//			LocalDate geboortedatum, Rol rol, String email, 
//			String wachtwoord) {
//		gb.addGebruiker(new Gebruiker(naam, voornaam, 
//				geboortedatum, rol, email, wachtwoord));
//	}
//	
//	public void voegEvenementFavoriet(long gebruikerId, 
//			long evenementId) {
//		
//		gb.geefSpecifiekGebruiker(gebruikerId)
//		.voegEvenementFavoriet(eb.geefSpecifiekEvenement(evenementId)); 
//	}
//    
//	// *-------------------removing----------------------*
//
//	public void removeGebruiker(long id) {
//		gb.removeGebruiker(id);
//	}  
//	
//	public void verwijderEvenementFavoriet(long gebruikerId, 
//			long evenementId) {
//		
//		gb.geefSpecifiekGebruiker(gebruikerId)
//		.verwijderEvenementFavoriet(eb.geefSpecifiekEvenement(evenementId)); 
//	}
//	
//	public void verwijderAlleFavorieten(long gebruikerId) {
//		gb.geefSpecifiekGebruiker(gebruikerId).verwijderAlleFavorieten(); 
//	}
//	
//	// *-------------------getting----------------------*
//	
//	public List<IGebruiker> geefAlleGebruikers() {
//		List<? extends IGebruiker> gebruikers = gb.geefAlleGebruikers();
//	    return new ArrayList<>(gebruikers);    
//	 }
//	
//	public IGebruiker geefSpecifiekGebruiker(long id) {
//		return gb.geefSpecifiekGebruiker(id); 
//	}
//	
//	public Set<IEvenement> geefFavorietenVanGebruiker(long id){
//		return geefSpecifiekGebruiker(id).getFavorieteEvenementen(); 
//	}
//
//}
