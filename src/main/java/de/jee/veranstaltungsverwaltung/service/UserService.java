package de.jee.veranstaltungsverwaltung.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.model.Nutzer;

@ApplicationScoped
@Named
public class UserService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3726388823135287805L;

	@Inject
	private NutzerDAO nutzerDAO;

	public Nutzer findByUsername(String benutzername) {
		
		return nutzerDAO.findByUsername(benutzername);
	}
	
	public boolean checkPasswort(Nutzer nutzer, String passwort){
		return nutzerDAO.checkPassword(nutzer.getBenutzername(), passwort);
		
	}

	public boolean checkUser(String benutzername) {
				
		return nutzerDAO.findByUsername(benutzername)==null;
	}

	public Nutzer registerUser(String benutzername, String passwort1, boolean istManager, String email) {
		Nutzer nutzer = new Nutzer(benutzername, passwort1, istManager, email);
		nutzerDAO.save(nutzer);
		return nutzer;
	}
	public void setNutzerDAO(NutzerDAO nutzerDAO) {
		this.nutzerDAO = nutzerDAO;
	}
}
