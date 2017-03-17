package de.jee.veranstaltungsverwaltung.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.NutzerDAO;

@ApplicationScoped
@Named
public class UserService {

	public Nutzer findByUsername(String benutzername) {
		NutzerDAO dao=new NutzerDAO();
		
		return dao.findByUsername(benutzername);
	}
	
	public static boolean checkPasswort(Nutzer nutzer, String passwort){
		NutzerDAO userDAO = new NutzerDAO();
		return userDAO.checkPassword(nutzer.getBenutzername(), passwort);
		
	}

	public static boolean checkUser(String benutzername) {
		NutzerDAO userDAO = new NutzerDAO();
				
		return userDAO.findByUsername(benutzername)==null;
	}

	public static Nutzer registerUser(String benutzername, String passwort1, boolean istManager, String email) {
		NutzerDAO userDAO = new NutzerDAO();
		Nutzer nutzer = new Nutzer(benutzername, passwort1, istManager, email);
		userDAO.save(nutzer);
		return nutzer;
	}
	
	
	

}
