package de.jee.veranstaltungsverwaltung.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;

public class NutzerDAO {
	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	private static final Logger logger = Logger.getLogger(NutzerDAO.class);
	/**
	 * Speichert den übergebenen Nutzer in der Datenbank. 
	 * @param nutzer Der Nutzer der in der Datenbank gepeichert werden soll.
	 * @return Wenn der Nutzer gespeiche
	 */
	public int save(Nutzer nutzer){
		int returncode = -1;
		nutzer.setBenutzername(nutzer.getBenutzername().toLowerCase());
		Session session = null;
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
			int temp = (int) session.save(nutzer);
			session.getTransaction().commit();	
			returncode = temp;
		}
		catch(Exception e){
			logger.log(Level.DEBUG,"Der Benutzer mit dem Benuternamen: " + nutzer.getBenutzername() + " konnte nicht gespeichert werden:\n" + e.getStackTrace());
		}
		finally{
			session.close();
		}
		return returncode;
	}
	/**
	 * Diese Methode sucht einen Nutzer auf Basis der ID in der Datenbank.
	 * @param id Die ID des Nutzer-Objektes nach dem gesucht werden soll.
	 * @return Das Nutzer-Objekt falls das Objekt vorhanden ist oder Null falls das Objekt nicht vorhanden ist / bezogen werden konnte. 
	 */
	public Nutzer findByID(int id){
		Nutzer nutzer = new Nutzer();
		Session session = null;
		try{
			session = sessionFactory.openSession();
			nutzer = session.get(Nutzer.class, id);
		}
		catch(NullPointerException np){
			logger.log(Level.INFO, "Es befindet sich kein Benutzer mit der ID: " + id + " in der Datenbank");
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Der Benutzer mit der ID: " + id + " konnte nicht bezogen werden:\n" + e.getStackTrace());
		}
		finally{
			session.close();
		}
		if(nutzer.getBenutzername() == null)
			return null;
		return nutzer;
	}
	/**
	 * Diese Methode sucht einen Nutzer auf Basis des Benutzernamen in der Datenbank.
	 * @param benutzername Der Benutzername des Nutzer-Objektes nach dem gesucht werden soll.
	 * @return Das Nutzer-Objekt falls das Objekt vorhanden ist oder Null falls das Objekt nicht vorhanden ist / bezogen werden konnte. 
	 */
	public Nutzer findByUsername(String benutzername){
		benutzername = benutzername.toLowerCase();
		Nutzer nutzer = new Nutzer();
		Session session = null;
		try{
			session = sessionFactory.openSession();
			String hql = "from Nutzer n where n.benutzername='" + benutzername + "'";
			List<?> nutzerListe = session.createQuery(hql).list();
			if(nutzerListe.size() == 1)
				nutzer = (Nutzer) nutzerListe.get(0);
			else
				logger.log(Level.INFO, "Der Benutzer mit dem Benutzernamen: " + benutzername + " ist nicht vorhanden");
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Der Benutzer mit dem Benutzernamen: " + benutzername + " konnte nicht bezogen werden:\n" + e.getStackTrace());
		}
		finally{
			session.close();
		}
		if(nutzer.getBenutzername() == null)
			return null;
		return nutzer;
	}
	
	public boolean changePassword(String benutzername, String altesPasswort, String neuesPasswort){
		benutzername = benutzername.toLowerCase();
		if(!this.checkPassword(benutzername, altesPasswort))
			return false;
		Session session = null;
		try{
			Nutzer nutzer = this.findByUsername(benutzername);
			session = sessionFactory.openSession();
			session.getTransaction().begin();
			nutzer.setPasswort(neuesPasswort);
			session.update(nutzer);
			session.getTransaction().commit();			
		}
		catch(NullPointerException np){
			logger.log(Level.INFO, "Der Benutzer mit dem Benutzernamen: " + benutzername + " konnte nicht bezogen werden");
			return false;
		}
		catch(Exception e){
			logger.log(Level.DEBUG, "Das Passwort konnte nicht aktualisiert werden.\n" + e.getStackTrace());
			return false;
		}
		finally{
			session.close();
		}
		return true;
	}
	public boolean checkPassword(String benutzername, String passwort){
		benutzername = benutzername.toLowerCase();
		try{
			Nutzer nutzer = this.findByUsername(benutzername);
			if(!nutzer.getPasswort().equals(passwort))
				return false;
		}
		catch(NullPointerException np){
			logger.log(Level.INFO, "Der Benutzer mit dem Benutzernamen: " + benutzername + " konnte nicht bezogen werden");
			return false;
		}
		return true;
	}
}
