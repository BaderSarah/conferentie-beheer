package domein.evenement;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// beheer van de klassen Evenement, Lokaal en Spreker
//public class EvenementBeheerder {
//
//    public final String PERSISTENCE_UNIT_NAME = "conferentie";
//	private EntityManager em;
//    private EntityManagerFactory emf;
//    
//    public EvenementBeheerder() {
//        initializePersistentie();
//    }
//    
//    private void initializePersistentie() {
//        openPersistentie();
//        EvenementData od = new EvenementData(this);
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
//    public void addEvenement(Evenement e) {
//        em.getTransaction().begin();
//        em.persist(e);
//        em.getTransaction().commit();
//    }
//    
//    public void addLokaal(Lokaal l) {
//        em.getTransaction().begin();
//        em.persist(l);
//        em.getTransaction().commit();
//    }
//
//    
//    public void addSpreker(Spreker s) {
//        em.getTransaction().begin();
//        em.persist(s);
//        em.getTransaction().commit();
//    }
//
//    
//	// *-------------------removing----------------------*
//
//    public void removeEvenement(long id) {
//        em.getTransaction().begin();
//        Evenement e = em.find(Evenement.class, id);
//        if (e != null) em.remove(e);
//        em.getTransaction().commit();
//    }
//    
//    public void removeLokaal(long id) {
//        em.getTransaction().begin();
//        Lokaal l = em.find(Lokaal.class, id);
//        if (l != null) em.remove(l);
//        em.getTransaction().commit();
//    }
//
//    public void removeSpreker(long id) {
//        em.getTransaction().begin();
//        Spreker s = em.find(Spreker.class, id);
//        if (s != null) em.remove(s);
//        em.getTransaction().commit();
//    }
//    
//	// *-------------------getting----------------------*
//
//    public List<? extends IEvenement> geefAlleEvenementen() {
//    	return em.createQuery("SELECT e FROM Evenement e", Evenement.class)
//    			.getResultList(); 
//    }
//    
//    public List<? extends ILokaal> geefAlleLokalen() {
//        return em.createQuery("SELECT l FROM Lokaal l", Lokaal.class).getResultList();
//    }
//
//    
//    public List<? extends ISpreker> geefAlleSprekers() {
//        return em.createQuery("SELECT s FROM Spreker s", Spreker.class).getResultList();
//    }
//    
//    public Evenement geefSpecifiekEvenement(long id) {
//        return em.find(Evenement.class, id);
//    }
//    
//    public Lokaal geefSpecifiekLokaal(long id) {
//        return em.find(Lokaal.class, id);
//    }
//    
//    public Spreker geefSpecifiekSpreker(long id) {
//        return em.find(Spreker.class, id);
//    }
//}
