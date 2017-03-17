package de.jee.veranstaltungsverwaltung.view;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.service.EventService;

@Named
@RequestScoped
public class AllEventsRequest {
	
	@Inject
	private Security security;

	private Veranstaltung events=EventService.holeAlleVeranstaltungen().get(0);
	






	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Veranstaltung getEvents() {
		return events;
	}

	public void setEvents(Veranstaltung events) {
		this.events = events;
	}
}
