package de.jee.veranstaltungsverwaltung.controller;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.model.NutzerDAO;
@ManagedBean(name="test")
@RequestScoped
public class Test {
	private String message;
	public String doTest() {
		Nutzer nutzer = new Nutzer("jan", "test", true);
		NutzerDAO dao = new NutzerDAO();
		int id = dao.save(nutzer);
		if(id < 0)
			message = "Der Benutzer [ID=" + id + ", Benutzername="  + nutzer.getBenutzername()+ ", Passwort=" + nutzer.getPasswort() + "] wurde erfolgreich angelegt";
		else
			message = "Der Benutzer konnte nicht angelegt werden...";
		return "Test.jsf";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
