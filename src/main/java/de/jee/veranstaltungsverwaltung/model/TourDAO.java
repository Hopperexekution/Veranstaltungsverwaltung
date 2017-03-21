package de.jee.veranstaltungsverwaltung.model;

import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;

public class TourDAO {
	private static final Logger logger = Logger.getLogger(TourDAO.class);
	
	public int save(Tour tour){
		int returncode = -1;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(tour);
			em.getTransaction().commit();	
			returncode = tour.getId();
		}
		catch(Exception e){
			logger.log(Level.DEBUG,"Die Tour mit dem Namen: " + tour.getName() + " konnte nicht gespeichert werden:\n" + e.getStackTrace());
		}
		finally{
			em.close();
		}		
		return returncode;
	}

}
