package de.jee.veranstaltungsverwaltung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;
import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.Reservierung;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
@Named
@ApplicationScoped
public class ReservierungDAO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6793709509860988991L;
	private static final Logger logger = Logger.getLogger(ReservierungDAO.class);
	/**
	 * Bezieht die Reservierung auf Basis der ID
	 * @param id Die Reservierungs-ID zum zugeh�rigen Reservierungs-Objekt
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
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Pr�fen sie die Datenbankenstellungen und die persistence.xml");
		
		}
		return reservierung;
	}
	/**
	 * Bezieht die Reservierungen, die zu einem Benutzer geh�ren
	 * @param nutzer Der Benutzer zu dem die Reservierungen bezogen werden sollen.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Reservierung> selectbyUserID(Nutzer nutzer){
		List<Reservierung> reservierungen = null;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			String hql = "from Reservierung as r where r.nutzer="+nutzer.getId();
			reservierungen = em.createQuery(hql).getResultList();

		}
		catch(NullPointerException np){
			logger.log(Level.INFO, "Die Reservierungen zu dem Nutzer mit der ID: " + nutzer.getId() + " befinden sich nicht in der Datenbank");
			return null;
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Die Reservierungen zu dem Nutzer mit der ID: " + nutzer.getId() + " konnten nicht bezogen werden\n" + e.getStackTrace());
			return null;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Pr�fen sie die Datenbankenstellungen und die persistence.xml");
		
		}
		return reservierungen;
	}
	/**
	 * Bezieht die Tickets zu der zugeh�rigen Reservierung. Dazu wird die Reservierungs-ID verwendet.
	 * @param reservierungsID Die Reservierungs-ID nach der die Tickets gesucht werden sollen.
	 * @return Eine Liste mit den Tickets, null falls sie nicht bezogen werden konnten. Die Liste ist leer, wenn es zur Reservierung keine Tickets gibt. 
	 */
	@SuppressWarnings("unchecked")
	public List<Ticket> getTicketsByReservierungsID(int reservierungsID){
		List<Ticket> tickets = null;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			em = HibernateUtil.getEntityManager();
			String hql = "from Ticket as t where t.reservierung="+reservierungsID;
			tickets = em.createQuery(hql).getResultList();
			
//			CriteriaBuilder cb = HibernateUtil.getCriteriaBuilder();
//			CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
//			Root<Ticket> root = query.from(Ticket.class);
//			query.select(root);
//			query.where(cb.equal(root.get("reservierung").as(Integer.class), reservierungsID));
//			tickets = em.createQuery(query).getResultList();
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Die Tickets zu der Reservierungs-ID: " + reservierungsID + " konnten nicht bezogen werden");
			return null;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Pr�fen sie die Datenbankenstellungen und die persistence.xml");
		
		}
		return tickets;
	}
	/**
	 * Speichert die Reservierung an eine Veranstaltung und bindet Tickets an die Reservierung
	 * @param reservierung Die Reservierung die gespeichert werden soll
	 * @param veranstaltung Die Veranstaltung die gespeichert werden soll
	 * @param anzahlTickets Die Anzahl Tickets die gebunden werden sollen
	 * @return
	 */
	public int save(Reservierung reservierung, Veranstaltung veranstaltung, int anzahlTickets){
		int returncode = 0;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			List<Ticket> ticketListe = veranstaltung.getVerfuegbareTickets();
			if(anzahlTickets <= ticketListe.size()){
				em.getTransaction().begin();
				em.persist(reservierung);
				for(int i = 0; i < anzahlTickets; i++){
					Ticket ticket = ticketListe.get(i);
					ticket.setReservierung(reservierung);
					em.merge(ticket);
				}
				em.getTransaction().commit();
				returncode = reservierung.getId();
			}
			else
				logger.log(Level.ERROR, "Es sind nicht gen�gend Tickets f�r die Reservierung verf�gbar!");
		}
		catch(Exception e){
			logger.log(Level.ERROR, "Die Reservierung konnte nicht durchgef�hrt werden");
			if(em != null && em.getTransaction().isActive())
				em.getTransaction().rollback();
			returncode = -1;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Pr�fen sie die Datenbankenstellungen und die persistence.xml");
		
		}
		return returncode;
	}
	/**
	 * Findet alle Reservierungen zu den �bergebenen Veranstaltungen
	 * @param events Die Veranstaltungen zu denen die Reservierungen gefunden werden sollen
	 * @return
	 */
	public List<Reservierung> findByEvents(List<Veranstaltung> events) {
		List<Reservierung> reservierungen = new ArrayList<Reservierung>();
		for(Veranstaltung event:events){
			List<Reservierung> rs = findeReservierungEvent(event);
			if (rs!=null)
			reservierungen.addAll(rs);
		}
		return reservierungen;
	}
	/**
	 * Findet alle Reservierungen zu einer Veranstaltung
	 * @param event Die Veranstaltung zu der die Reservierungen gefunden werden sollen
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Reservierung> findeReservierungEvent(Veranstaltung event) {
		List<Reservierung> reservierungen = null;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			em = HibernateUtil.getEntityManager();
			String hql = "from Reservierung as r inner join fetch r.tickets as t inner join fetch t.veranstaltung as v where v.id="+ event.getId() + "group by r.id";
			reservierungen = em.createQuery(hql).getResultList();
			

		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Die Reservierungen zu der Event-ID: " + event.getId() + " konnten nicht bezogen werden");
			return null;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Pr�fen sie die Datenbankenstellungen und die persistence.xml");
		}
		return reservierungen;
	}
}
