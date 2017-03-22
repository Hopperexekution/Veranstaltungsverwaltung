package de.jee.veranstaltungsverwaltung.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;

@Named
@RequestScoped
public class DetailVeranstaltungRequest {
	@Inject
	private Security security;

	private Veranstaltung event;

	public String erstelleVeranstaltung() {

		return "neue_veranstaltung";
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

}
