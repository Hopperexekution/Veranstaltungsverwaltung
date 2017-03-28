package de.jee.veranstaltungsverwaltung.view;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.VeranstaltungDAO;

@Named
@ViewScoped
public class DetailVeranstaltungRequest implements Serializable {
	@Inject
	private Security security;

	private Veranstaltung event;
	
	private boolean canEdit;

	
	public void editVeranstaltung() {

		canEdit=true;
	}
	public void updateVeranstaltung() {

		System.out.println("Methode update Veranstaltung");
		System.out.println(event.getName());
		VeranstaltungDAO dao= new VeranstaltungDAO();
		dao.update(event);
		
		
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Veranstaltung getEvent() {
		return event;
	}

	public void setEvent(Veranstaltung event) {
		this.event = event;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

}
