package de.jee.veranstaltungsverwaltung.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;
import de.jee.veranstaltungsverwaltung.model.Ticket;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
@Named
@ApplicationScoped
public class TicketDAO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9050853981754743506L;
	private Logger logger = Logger.getLogger(TicketDAO.class);
	/**
	 * Findet ein Ticket auf Basis seiner ID
	 * @param id Die ID des Tickets
	 * @return Das Ticket
	 */
	public Ticket findByID(int id){
		Ticket ticket = null;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			ticket = em.find(Ticket.class, id);		
		}
		catch(NullPointerException np){
			logger.log(Level.INFO, "Das Ticket mit der ID: " + id + " befindet sich nicht in der Datenbank.");
			return null;
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Das Ticket mit der ID: " + id + " konnte nicht bezogen werden \n" + e.getStackTrace());
			return null;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

		}
		return ticket;
	}
	/**
	 * Findet alle Tickets zu allen Veranstaltungen
	 * @param veranstaltung Die Veranstaltung zu der die Tickets gesucht werden sollen
	 * @return Die Tickets einer Veranstaltung
	 */
	public List<Ticket> alleTicketsEinerVeranstaltung(Veranstaltung veranstaltung){
		if(veranstaltung.getId() == 0)
			return null;
		List<Ticket> tickets = null;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			CriteriaBuilder cb = HibernateUtil.getCriteriaBuilder();
			CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
			Root<Ticket> root = query.from(Ticket.class);
			query.select(root);
			query.where(cb.equal(root.get("veranstaltung").as(Integer.class), veranstaltung.getId()));
			tickets = em.createQuery(query).getResultList();
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Die Tickets zu der Veranstaltung mit der ID: " + veranstaltung.getId() + " konnten nicht aus der Datenbank geladen werden\n" + e.getStackTrace());
			return null;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

		}
		return tickets;		
	}
	/**
	 * Löscht ein Ticket
	 * @param ticket Das Ticket das gelöscht werden soll
	 * @return
	 */
	public int loescheTicket(Ticket ticket){
		int returncode = 0;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			em.getTransaction().begin();
			String hqlDelete = "delete Ticket where id = :id";
			int deletedEntities = em.createQuery( hqlDelete )
			                            .setParameter( "id", ticket.getId() )
			                            .executeUpdate();
			returncode=deletedEntities;
			em.getTransaction().commit();
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Das Ticket konnte nicht gelöscht werdenn \n" + e.getStackTrace());
			returncode = -1;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

		}
		return returncode;
	}
	/**
	 * Speichert ein Ticket
	 * @param ticket Das Ticket das gespeichert werden soll
	 * @return Der Returncode ob es erfolgrich war -1 war nicht erfolgreich 0 war erfolgreich 
	 */
	public int save(Ticket ticket){
		int returncode = 0;
		EntityManager em = null;
		try{
			em = HibernateUtil.getEntityManager();
			em.getTransaction().begin();
			em.persist(ticket);

			em.getTransaction().commit();
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Das Ticket konnte nicht gespeichert werdenn \n" + e.getStackTrace());
			returncode = -1;
		}
		finally{
			if(em != null)
				em.close();
			else
				logger.log(Level.DEBUG, "Der Entity Manager war null --> Folglich konnte er warscheinlich nicht erzeugt werden. Prüfen sie die Datenbankenstellungen und die persistence.xml");

		}
		return returncode;
	}
}
