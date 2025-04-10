package domein;

//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import domein.evenement.Evenement;
//import domein.evenement.EvenementBeheerder;
//import domein.evenement.IEvenement;
//import domein.evenement.ILokaal;
//import domein.evenement.ISpreker;
//import domein.evenement.Lokaal;
//import domein.evenement.Spreker;

//public class EvenementController {
//
//	private EvenementBeheerder eb = new EvenementBeheerder(); 
//	
//    public void close() {
//        eb.closePersistentie();
//    }
//	
//	// *-------------------adding----------------------*
//
//	public void addEvenement(String naam, String beschrijving, 
//			int beamercode, int beamercheck, double prijs, 
//			LocalDate datum, LocalTime beginTijdstip, 
//			long lokaalId, int[] sprekersIds) {
//		
//		Lokaal lokaal = eb.geefSpecifiekLokaal(lokaalId); 
//		
//		Set<Spreker> sprekers = new HashSet<>(); 
//		
//		for (int i = 0; i < sprekersIds.length; i++) {
//			sprekers.add(eb.geefSpecifiekSpreker(sprekersIds[i])); 
//		}
//		
//		eb.addEvenement(new Evenement(naam, beschrijving, beamercode, 
//				beamercheck, prijs, datum, beginTijdstip, beginTijdstip, 
//				lokaal, sprekers));
//	}
//	
//	public void addLokaal(String naam, int capaciteit) {
//		eb.addLokaal(new Lokaal(naam, capaciteit));
//	}
//	
//	public void addSpreker(String naam, String voornaam, String email) {
//		eb.addSpreker(new Spreker(naam, voornaam, email));
//	}
//	
//	// *-------------------removing----------------------*
//
//	public void removeEvenement(long id) {
//		eb.removeEvenement(id);
//	}
//	
//	public void removeSpreker(long id) {
//		eb.removeSpreker(id);
//	}
//	
//	public void removeLokaal(long id) {
//		eb.removeLokaal(id);
//	}
//	
//	// *-------------------getting----------------------*
//	
//	public List<IEvenement> geefAlleEvenementen() {
//		List<? extends IEvenement> evenementen = eb.geefAlleEvenementen();
//	    return new ArrayList<>(evenementen);    
//	 }
//	
//	public List<ILokaal> geefAlleLokalen() {
//		List<? extends ILokaal> lokalen = eb.geefAlleLokalen();
//	    return new ArrayList<>(lokalen);    
//	 }
//	
//	public List<ISpreker> geefAlleSprekers() {
//		List<? extends ISpreker> sprekers = eb.geefAlleSprekers();
//	    return new ArrayList<>(sprekers);    
//	 }
//	
//	public IEvenement geefSpecifiekEvenement(long id) {
//		return eb.geefSpecifiekEvenement(id); 
//	}
//	
//	public ILokaal geefSpecifiekLokaal(long id) {
//		return eb.geefSpecifiekLokaal(id); 
//	}
//	
//	public ISpreker geefSpecifiekSpreker(long id) {
//		return eb.geefSpecifiekSpreker(id); 
//	}
//
//}
