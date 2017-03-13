package de.jee.veranstaltungsverwaltung.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;

public class ReservierungDAO {
	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static final Logger logger = Logger.getLogger(ReservierungDAO.class);
	/**
	 * Bezieht die Reservierung auf Basis der ID
	 * @param id Die Reservierungs-ID zum zugehörigen Reservierungs-Objekt
	 * @return Das Reservierungs-Objekt oder Null, falls es nicht bezogen werden konnte.
	 */
	public Reservierung findByID(int id){
		Reservierung reservierung = null;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			session.get(Reservierung.class, id);
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
			session.close();
		}
		return reservierung;
	}
	@SuppressWarnings("unchecked")
	/**
	 * Bezieht die Tickets zu der zugehörigen Reservierung. Dazu wird die Reservierungs-ID verwendet.
	 * @param reservierungsID Die Reservierungs-ID nach der die Tickets gesucht werden sollen.
	 * @return Eine Liste mit den Tickets, null falls sie nicht bezogen werden konnten. Die Liste ist leer, wenn es zur Reservierung keine Tickets gibt. 
	 */
	public List<Ticket> getTicketsByReservierungsID(int reservierungsID){
		List<Ticket> tickets = null;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			String hql = "from Ticket t where t.reservierung=" + reservierungsID;
			tickets = session.createQuery(hql).list();
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Die Tickets zu der Reservierungs-ID: " + reservierungsID + " konnten nicht bezogen werden");
			return null;
		}
		finally{
			session.close();
		}
		return tickets;
	}
}
