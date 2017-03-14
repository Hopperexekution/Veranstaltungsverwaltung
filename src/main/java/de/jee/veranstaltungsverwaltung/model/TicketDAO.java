package de.jee.veranstaltungsverwaltung.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;

public class TicketDAO {
	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private Logger logger = Logger.getLogger(TicketDAO.class);
	
	public Ticket findByID(int id){
		Ticket ticket = null;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			ticket = session.get(Ticket.class, id);		
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
			session.close();
		}
		return ticket;
	}
	public List<Ticket> alleTicketsEinerVeranstaltung(Veranstaltung veranstaltung){
		List<Ticket> tickets = null;
		Session session = null;
		try{
			session = sessionFactory.openSession();
			tickets = session.createCriteria(Ticket.class).add(Restrictions.eq("veranstaltung", veranstaltung.getId())).list();
		}
		catch(NullPointerException np){
			
		}
		catch(Exception e){
			
		}
		return tickets;		
	}
}
