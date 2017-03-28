package de.jee.veranstaltungsverwaltung.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;

public class TicketDAO {
	private Logger logger = Logger.getLogger(TicketDAO.class);
	
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
			em.close();
		}
		return ticket;
	}
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
		return tickets;		
	}
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
			em.close();
		}
		return returncode;
	}
	
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
			em.close();
		}
		return returncode;
	}
}
