package domein.gebruiker;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

//beheer van de klasse Gebruiker
//public class GebruikerBeheerder {
//
//	public final String PERSISTENCE_UNIT_NAME = "conferentie";
//	private EntityManager em;
//    private EntityManagerFactory emf;
//    
//    public GebruikerBeheerder() {
//        initializePersistentie();
//    }
//    
//    private void initializePersistentie() {
//        openPersistentie();
//        GebruikerData od = new GebruikerData(this);
//        od.populeerData();
//    }
//
//    private void openPersistentie() {
//        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//        em = emf.createEntityManager();
//    }
//
//    public void closePersistentie() {
//        em.close();
//        emf.close();
//    }
//    
//	// *-------------------adding----------------------*
//
//    public void addGebruiker(Gebruiker g) {
//    	em.getTransaction().begin();
//        em.persist(g);
//        em.getTransaction().commit();
//    }
//    
//	// *-------------------removing----------------------*
//    
//    public void removeGebruiker(long id) {
//    	em.getTransaction().begin();
//        Gebruiker g = em.find(Gebruiker.class, id);
//        if (g != null) em.remove(g);
//        em.getTransaction().commit();
//    }
//    
//	// *-------------------getting----------------------*
//
//    public List<? extends IGebruiker> geefAlleGebruikers() {
//        return em.createQuery("SELECT g FROM Gebruiker g", Gebruiker.class).getResultList();
//    }
//    
//    public Gebruiker geefSpecifiekGebruiker(long id) {
//        return em.find(Gebruiker.class, id);
//    }
//}
