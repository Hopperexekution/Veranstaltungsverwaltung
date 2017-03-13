package de.jee.veranstaltungsverwaltung.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;
public class VeranstaltungDAO {
		private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		private static final Logger logger = Logger.getLogger(VeranstaltungDAO.class);
		
		public Veranstaltung findByID(int id){
			Veranstaltung veranstaltung = null;
			Session session = null;
			try{
				session = sessionFactory.openSession();
				session.get(Veranstaltung.class, id);
			}
			catch(NullPointerException np){
				logger.log(Level.INFO, "Es befindet sich keine Veranstaltung mit der ID: " + id + " in der Datenbank");
				return null;
			}
			catch(Exception e){
				logger.log(Level.DEBUG, "Die Veranstaltung mit der ID: " + id + " konnte nicht bezogen werden\n" + e.getStackTrace());
				return null;
			}
			finally{
				session.close();
			}
			return veranstaltung;
		}
		
		public List<Veranstaltung> findByName(String name){
			List<Veranstaltung> veranstaltungen = null;
			
			return veranstaltungen;
		}
}
