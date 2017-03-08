package de.jee.veranstaltungsverwaltung.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.hibernate.Session;
import org.hibernate.query.Query;

import de.jee.veranstaltungsverwaltung.model.Nutzer;
import de.jee.veranstaltungsverwaltung.controller.HibernateUtil;
@ManagedBean(name="test")
@RequestScoped
public class Test {
	private String message;
	public String doTest() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Nutzer nutzer = new Nutzer("jan", "test", true);
		session.beginTransaction();
		session.save(nutzer);
		session.getTransaction().commit();
		Query query = session.createQuery("from Nutzer n where n.benutzername ='jan'");
		Nutzer gespeicherterNutzer = (Nutzer) query.getSingleResult();
		setMessage("ID: " + gespeicherterNutzer.getId() + " Benutzerame: " + gespeicherterNutzer.getBenutzername());
		return "Test.jsf";
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
