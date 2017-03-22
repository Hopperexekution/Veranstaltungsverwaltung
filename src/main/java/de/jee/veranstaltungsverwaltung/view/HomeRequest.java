package de.jee.veranstaltungsverwaltung.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.jee.veranstaltungsverwaltung.controller.Security;
import de.jee.veranstaltungsverwaltung.model.Veranstaltung;
import de.jee.veranstaltungsverwaltung.model.VeranstaltungDAO;

@Named
@RequestScoped
public class HomeRequest {

	@Inject
	private Security security;
	@Inject
	private VeranstaltungDAO dao = new VeranstaltungDAO();
	private List<Veranstaltung> naechsteEvents = new ArrayList<Veranstaltung>(Arrays.asList(
            new Veranstaltung("Test1", "Beschreibung1", new Date(), true, null),
            new Veranstaltung("Test2", "Beschreibung2", new Date(), true, null),
            new Veranstaltung("Test3", "Beschreibung3", new Date(), true, null)));

	//private List<Veranstaltung> meistReservierteEvents = dao.mostReserved();
	
	private List<Veranstaltung> meistReservierteEvents = dao.mostReserved();
	
	private List<Veranstaltung> neuesteEvents = dao.latestFive();

	public List<Veranstaltung> getNaechsteEvents() {
		return naechsteEvents;
	}

	public void setNaechsteEvents(List<Veranstaltung> naechsteEvents) {
		this.naechsteEvents = naechsteEvents;
	}

	public List<Veranstaltung> getMeistReservierteEvents() {
		return meistReservierteEvents;
	}

	public void setMeistReservierteEvents(List<Veranstaltung> meistReservierteEvents) {
		this.meistReservierteEvents = meistReservierteEvents;
	}

	public List<Veranstaltung> getNeuesteEvents() {
		return neuesteEvents;
	}

	public void setNeuesteEvents(List<Veranstaltung> neuesteEvents) {
		this.neuesteEvents = neuesteEvents;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

}
