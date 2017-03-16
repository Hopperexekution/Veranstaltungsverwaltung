package de.jee.veranstaltungsverwaltung.model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;

public class ReservierungDAO {
	private static final Logger logger = Logger.getLogger(ReservierungDAO.class);
	/**
	 * Bezieht die Reservierung auf Basis der ID
	 * @param id Die Reservierungs-ID zum zugehörigen Reservierungs-Objekt
	 * @return Das Reservierungs-Objekt oder Null, falls es nicht bezogen werden konnte.
	 */
	public Reservierung findByID(int id){
		Reservierung reservierung = null;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			reservierung = em.find(Reservierung.class, id);
		}
		catch(NullPointerException np){
			logger.log(Level.INFO, "Die Reservierung mit der ID: " + id + " befindet sich nicht in der Datenbank");
			return null;
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Die Reservierung mit der ID: " + id + " konnte nicht bezogen werden\n" + e.getStackTrace());
			return null;
		}
		finally{
			em.close();
		}
		return reservierung;
	}
	/**
	 * Bezieht die Tickets zu der zugehörigen Reservierung. Dazu wird die Reservierungs-ID verwendet.
	 * @param reservierungsID Die Reservierungs-ID nach der die Tickets gesucht werden sollen.
	 * @return Eine Liste mit den Tickets, null falls sie nicht bezogen werden konnten. Die Liste ist leer, wenn es zur Reservierung keine Tickets gibt. 
	 */
	public List<Ticket> getTicketsByReservierungsID(int reservierungsID){
		List<Ticket> tickets = null;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			CriteriaBuilder cb = HibernateUtil.getCriteriaBuilder();
			CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
			Root<Ticket> root = query.from(Ticket.class);
			query.select(root);
			query.where(cb.equal(root.get("reservierung").as(Integer.class), reservierungsID));
			tickets = em.createQuery(query).getResultList();
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Die Tickets zu der Reservierungs-ID: " + reservierungsID + " konnten nicht bezogen werden");
			return null;
		}
		finally{
			em.close();
		}
		return tickets;
	}
}
